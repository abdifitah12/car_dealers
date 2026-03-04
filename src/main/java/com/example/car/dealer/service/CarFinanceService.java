package com.example.car.dealer.service;

import com.example.car.dealer.carEnum.CarAvailability;
import com.example.car.dealer.entity.Car;
import com.example.car.dealer.entity.CarFinance;
import com.example.car.dealer.entity.FinancePerson;
import com.example.car.dealer.repository.CarFinanceRepository;
import com.example.car.dealer.repository.CarRepository;
import com.example.car.dealer.repository.FinancePersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CarFinanceService {

    private final CarFinanceRepository financeRepo;
    private final CarRepository carRepository;
    private final FinancePersonRepository personRepository;

    @Transactional
    public CarFinance upsertFinanceWithPerson(
            Car car,
            Integer termMonths,
            Double aprPercent,
            Double downPayment,
            Integer missedMonths,
            String fullName,
            String phone,
            String email,
            String address
    ) {
        // ✅ validations
        if (car == null || car.getId() == null) {
            throw new IllegalArgumentException("Car is required.");
        }
        if (car.getPrice() == null) {
            throw new IllegalArgumentException("Car price is required.");
        }
        if (termMonths == null || termMonths <= 0) {
            throw new IllegalArgumentException("Term months must be > 0.");
        }
        if (aprPercent == null || aprPercent < 0) {
            throw new IllegalArgumentException("APR must be >= 0.");
        }
        if (downPayment == null || downPayment < 0) downPayment = 0.0;
        if (missedMonths == null || missedMonths < 0) missedMonths = 0;

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }

        // ✅ 1) find or create finance for this car
        CarFinance finance = financeRepo.findByCarId(car.getId()).orElseGet(CarFinance::new);
        finance.setCar(car);

        // ✅ 2) person info (use existing person if already linked)
        FinancePerson person = (finance.getPerson() != null) ? finance.getPerson() : new FinancePerson();
        person.setFullName(fullName.trim());
        person.setPhone(phone.trim());
        person.setEmail(email.trim());
        person.setAddress(address != null ? address.trim() : null);

        // because CarFinance has cascade = ALL for person, this save is optional,
        // but keeping it is fine and explicit:
        person = personRepository.save(person);
        finance.setPerson(person);

        // ✅ 3) store missed months
        finance.setMissedMonths(missedMonths);

        // ✅ 4) rule: each missed month increases APR by +1%
        double effectiveApr = aprPercent + (missedMonths * 1.0);

        // ✅ 5) calculate payments
        double price = car.getPrice();
        double principal = Math.max(0, price - downPayment);

        double monthlyPayment = calcMonthlyPayment(principal, effectiveApr, termMonths);
        double totalPaid = (monthlyPayment * termMonths) + downPayment;
        double balanceOwed = principal;

        // ✅ 6) save finance values
        finance.setTermMonths(termMonths);
        finance.setApr(effectiveApr); // store effective APR
        finance.setDownPayment(downPayment);
        finance.setMonthlyPayment(round2(monthlyPayment));
        finance.setTotalPaid(round2(totalPaid));
        finance.setBalanceOwed(round2(balanceOwed));

        // ✅ 7) mark car FINANCE
        markAsFinance(car);

        // ✅ 8) save finance
        return financeRepo.save(finance);
    }

    // ✅ APR amortization formula
    public double calcMonthlyPayment(double principal, double aprPercent, int termMonths) {
        if (principal <= 0) return 0.0;

        double r = (aprPercent / 100.0) / 12.0; // monthly interest rate
        if (r == 0) return principal / termMonths;

        double pow = Math.pow(1 + r, termMonths);
        return principal * (r * pow) / (pow - 1);
    }

    private double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public void markAsFinance(Car car) {
        car.setAvailability(CarAvailability.FINANCE);
        carRepository.save(car);
    }

    @Transactional
    public void payMonthly(Long financeId, Double paidAmount) {

        CarFinance finance = financeRepo.findById(financeId)
                .orElseThrow(() -> new RuntimeException("Finance not found"));

        if (finance.getTermMonths() == null || finance.getTermMonths() <= 0) {
            throw new RuntimeException("Loan already completed");
        }

        // ✅ if not provided, use expected monthly payment
        if (paidAmount == null || paidAmount <= 0) {
            paidAmount = finance.getMonthlyPayment();
        }

        double totalPaid = finance.getTotalPaid() == null ? 0.0 : finance.getTotalPaid();
        double balanceOwed = finance.getBalanceOwed() == null ? 0.0 : finance.getBalanceOwed();

        finance.setTotalPaid(round2(totalPaid + paidAmount));
        finance.setBalanceOwed(round2(balanceOwed - paidAmount));

        // ✅ decrease term every payment (your rule)
        finance.setTermMonths(finance.getTermMonths() - 1);

        if (finance.getBalanceOwed() <= 0 || finance.getTermMonths() <= 0) {
            finance.setBalanceOwed(Math.max(0.0, finance.getBalanceOwed()));
            finance.setTermMonths(Math.max(0, finance.getTermMonths()));
        }

        financeRepo.save(finance);
    }



}
