package com.entic.payroll.core.application.contract;

import com.entic.payroll.core.application.impl.CreateContractUseCaseImpl;
import com.entic.payroll.core.application.models.CreateContractUseCaseIn;
import com.entic.payroll.core.application.models.CreateContractUseCaseOut;
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

class CreateContractUseCaseTest {

    private final ContractRepositoryPort repositoryPort = mock(ContractRepositoryPort.class);
    private final CreateContractUseCaseImpl service = new CreateContractUseCaseImpl(repositoryPort);

    private static final UUID COMPANY_ID = UUID.randomUUID();
    private static final Long EMPLOYEE_ID = 1L;

    private CreateContractUseCaseIn validCommand() {
        return new CreateContractUseCaseIn(
                COMPANY_ID,
                EMPLOYEE_ID,
                ContractType.FIXED_TERM,
                new BigDecimal("1500000"),
                false,
                RiskLevel.TWO,
                LocalDate.of(2026, 1, 1),
                null
        );
    }

    @Test
    void shouldCreateContractSuccessfully() {
        CreateContractUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);
        when(repositoryPort.existsActiveForEmployee(EMPLOYEE_ID, null)).thenReturn(false);
        when(repositoryPort.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract c = invocation.getArgument(0);
            return Contract.reconstitute(1L, c.getCompanyId(), c.getEmployeeId(), c.getContractType(),
                    c.getBaseSalary(), c.isIntegralSalary(), c.getRiskLevelArl(),
                    c.getStartDate(), c.getEndDate());
        });

        CreateContractUseCaseOut result = service.execute(command);

        assertNotNull(result);
        assertNotNull(result.contractCreated());
        assertNotNull(result.contractCreated().id());
        assertEquals(COMPANY_ID, result.contractCreated().companyId());
        assertEquals(EMPLOYEE_ID, result.contractCreated().employeeId());
        assertEquals(ContractType.FIXED_TERM, result.contractCreated().contractType());
        assertEquals(new BigDecimal("1500000"), result.contractCreated().baseSalary());
        assertFalse(result.contractCreated().integralSalary());
        assertEquals(RiskLevel.TWO, result.contractCreated().riskLevelArl());
        assertEquals(LocalDate.of(2026, 1, 1), result.contractCreated().startDate());
        assertNull(result.contractCreated().endDate());
        verify(repositoryPort).save(any(Contract.class));
    }

    @Test
    void shouldFailWhenCompanyIdIsNull() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(null, EMPLOYEE_ID,
                ContractType.FIXED_TERM, new BigDecimal("1500000"), false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 1), null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.companyId.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEmployeeIdIsNull() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(COMPANY_ID, null,
                ContractType.FIXED_TERM, new BigDecimal("1500000"), false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 1), null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.employeeId.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenCompanyNotFound() {
        CreateContractUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(command));
        assertEquals("contract.company.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEmployeeNotFound() {
        CreateContractUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(command));
        assertEquals("contract.employee.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenContractTypeIsNull() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(COMPANY_ID, EMPLOYEE_ID,
                null, new BigDecimal("1500000"), false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 1), null);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.contractType.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenBaseSalaryIsNull() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(COMPANY_ID, EMPLOYEE_ID,
                ContractType.FIXED_TERM, null, false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 1), null);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.baseSalary.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenBaseSalaryIsZeroOrNegative() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(COMPANY_ID, EMPLOYEE_ID,
                ContractType.FIXED_TERM, BigDecimal.ZERO, false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 1), null);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.baseSalary.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenRiskLevelIsNull() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(COMPANY_ID, EMPLOYEE_ID,
                ContractType.FIXED_TERM, new BigDecimal("1500000"), false, null,
                LocalDate.of(2026, 1, 1), null);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.riskLevelArl.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenStartDateIsNull() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(COMPANY_ID, EMPLOYEE_ID,
                ContractType.FIXED_TERM, new BigDecimal("1500000"), false, RiskLevel.TWO,
                null, null);

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.startDate.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEndDateIsNotAfterStartDate() {
        CreateContractUseCaseIn command = new CreateContractUseCaseIn(COMPANY_ID, EMPLOYEE_ID,
                ContractType.FIXED_TERM, new BigDecimal("1500000"), false, RiskLevel.TWO,
                LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 1));

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("contract.dateRange.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEmployeeAlreadyHasActiveContract() {
        CreateContractUseCaseIn command = validCommand();

        when(repositoryPort.companyExists(COMPANY_ID)).thenReturn(true);
        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);
        when(repositoryPort.existsActiveForEmployee(EMPLOYEE_ID, null)).thenReturn(true);

        AlreadyExistsException ex = assertThrows(AlreadyExistsException.class, () -> service.execute(command));
        assertEquals("contract.active.alreadyExists", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
