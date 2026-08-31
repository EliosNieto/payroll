package com.entic.payroll.core.application.contract;

import com.entic.payroll.core.application.impl.UpdateContractUseCaseImpl;
import com.entic.payroll.core.application.models.UpdateContractUseCaseIn;
import com.entic.payroll.core.application.models.UpdateContractUseCaseOut;
import com.entic.payroll.core.application.port.out.ContractRepositoryPort;
import com.entic.payroll.core.domain.contract.Contract;
import com.entic.payroll.core.domain.enums.ContractType;
import com.entic.payroll.core.domain.enums.RiskLevel;
import com.entic.payroll.core.domain.errors.AlreadyExistsException;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateContractUseCaseTest {

    private final ContractRepositoryPort repositoryPort = mock(ContractRepositoryPort.class);
    private final UpdateContractUseCaseImpl service = new UpdateContractUseCaseImpl(repositoryPort);

    private static final Long ID = 5L;
    private static final UUID COMPANY_ID = UUID.randomUUID();
    private static final Long EMPLOYEE_ID = 1L;

    private UpdateContractUseCaseIn validCommand() {
        return new UpdateContractUseCaseIn(
                ID,
                ContractType.FIXED_TERM,
                new BigDecimal("1800000"),
                false,
                RiskLevel.THREE,
                LocalDate.of(2026, 3, 1),
                null
        );
    }

    @Test
    void shouldUpdateContractSuccessfully() {
        UpdateContractUseCaseIn request = validCommand();

        Contract existing = Contract.reconstitute(ID, COMPANY_ID, EMPLOYEE_ID, ContractType.FIXED_TERM,
                new BigDecimal("1500000"), false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 1), null);
        when(repositoryPort.findById(ID)).thenReturn(existing);
        when(repositoryPort.existsActiveForEmployee(EMPLOYEE_ID, ID)).thenReturn(false);
        when(repositoryPort.save(any(Contract.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateContractUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.contractUpdated());
        assertEquals(ID, result.contractUpdated().id());
        assertEquals(COMPANY_ID, result.contractUpdated().companyId());
        assertEquals(EMPLOYEE_ID, result.contractUpdated().employeeId());
        assertEquals(ContractType.FIXED_TERM, result.contractUpdated().contractType());
        assertEquals(new BigDecimal("1800000"), result.contractUpdated().baseSalary());
        assertEquals(RiskLevel.THREE, result.contractUpdated().riskLevelArl());
        assertEquals(LocalDate.of(2026, 3, 1), result.contractUpdated().startDate());
        verify(repositoryPort).save(any(Contract.class));
    }

    @Test
    void shouldFailWhenIdIsNull() {
        UpdateContractUseCaseIn request = new UpdateContractUseCaseIn(null,
                ContractType.FIXED_TERM, new BigDecimal("1800000"), false, RiskLevel.THREE,
                LocalDate.of(2026, 3, 1), null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("contract.id.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenContractNotFound() {
        UpdateContractUseCaseIn request = validCommand();

        when(repositoryPort.findById(ID)).thenThrow(new NotFoundException("id", ID.toString(), "contract.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("contract.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenContractTypeIsNull() {
        UpdateContractUseCaseIn request = new UpdateContractUseCaseIn(ID,
                null, new BigDecimal("1800000"), false, RiskLevel.THREE,
                LocalDate.of(2026, 3, 1), null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("contract.contractType.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenBaseSalaryIsNull() {
        UpdateContractUseCaseIn request = new UpdateContractUseCaseIn(ID,
                ContractType.FIXED_TERM, null, false, RiskLevel.THREE,
                LocalDate.of(2026, 3, 1), null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("contract.baseSalary.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenRiskLevelIsNull() {
        UpdateContractUseCaseIn request = new UpdateContractUseCaseIn(ID,
                ContractType.FIXED_TERM, new BigDecimal("1800000"), false, null,
                LocalDate.of(2026, 3, 1), null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("contract.riskLevelArl.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenStartDateIsNull() {
        UpdateContractUseCaseIn request = new UpdateContractUseCaseIn(ID,
                ContractType.FIXED_TERM, new BigDecimal("1800000"), false, RiskLevel.THREE,
                null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("contract.startDate.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEndDateIsNotAfterStartDate() {
        UpdateContractUseCaseIn request = new UpdateContractUseCaseIn(ID,
                ContractType.FIXED_TERM, new BigDecimal("1800000"), false, RiskLevel.THREE,
                LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 1));

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("contract.dateRange.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenAnotherActiveContractExists() {
        UpdateContractUseCaseIn request = validCommand();

        Contract existing = Contract.reconstitute(ID, COMPANY_ID, EMPLOYEE_ID, ContractType.FIXED_TERM,
                new BigDecimal("1500000"), false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 1), null);
        when(repositoryPort.findById(ID)).thenReturn(existing);
        when(repositoryPort.existsActiveForEmployee(EMPLOYEE_ID, ID)).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(request));
        assertEquals("contract.active.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
