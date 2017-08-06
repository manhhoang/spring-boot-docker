package com.concurrency.service;

import com.concurrency.model.Lender;
import com.concurrency.model.Loan;
import com.concurrency.repository.LenderRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class LoanServiceTest {

    @InjectMocks
    LoanService loanService;

    @Mock
    LenderRepository lenderRepository;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAvailableLoan() throws Exception {
        String marketFile = "lender_data.csv";
        double loanAmount = 1000;
        List<Lender> lenders = new ArrayList<>();
        Lender lender = new Lender();
        lender.setRate(0.069);
        lender.setAvailable(480);
        lenders.add(lender);

        lender = new Lender();
        lender.setRate(0.071);
        lender.setAvailable(520);
        lenders.add(lender);

        when(lenderRepository.findAllLendersSortedByRate(marketFile)).thenReturn(lenders);
        CompletableFuture<Loan> loanFuture = loanService.getAvailableLoan(marketFile, loanAmount);
        Loan loan = loanFuture.get();
        assertEquals("0.07003999999999999", String.valueOf(loan.getRate()));
    }
}
