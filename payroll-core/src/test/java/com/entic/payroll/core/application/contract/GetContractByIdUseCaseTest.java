package com.entic.payroll.core.application.contract;

import com.entic.payroll.core.application.impl.GetContractByIdUseCaseImpl;
import com.entic.payroll.core.application.models.GetContractByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetContractByIdUseCaseOut;
import com.entic.payroll.core.application.port.out.ContractRepositoryPort;
import com.entic.payroll.core.domain.contract.Contract;
import com.entic.payroll.core.domain.enums.ContractType;
import com.entic.payroll.core.domain.enums.RiskLevel;
import com.entic.payroll.core.domain.errors.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetContractByIdUseCaseTest {

    private final ContractRepositoryPort repositoryPort = mock(ContractRepositoryPort.class);
    private final GetContractByIdUseCaseImpl service = new GetContractByIdUseCaseImpl(repositoryPort);

    @Test
    void shouldGetContractByIdSuccessfully() {
        Long id = 5L;
        UUID companyId = UUID.randomUUID();
        Long employeeId = 1L;
        GetContractByIdUseCaseIn request = new GetContractByIdUseCaseIn(id);

        Contract contract = Contract.reconstitute(id, companyId, employeeId, ContractType.INDEFINITE_TERM,
                new BigDecimal("2500000"), true, RiskLevel.ONE,
                LocalDate.of(2026, 2, 1), LocalDate.of(2027, 2, 1));
        when(repositoryPort.findById(id)).thenReturn(contract);

        GetContractByIdUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.contractFound());
        assertEquals(id, result.contractFound().id());
        assertEquals(companyId, result.contractFound().companyId());
        assertEquals(employeeId, result.contractFound().employeeId());
        assertEquals(ContractType.INDEFINITE_TERM, result.contractFound().contractType());
        assertEquals(new BigDecimal("2500000"), result.contractFound().baseSalary());
        assertTrue(result.contractFound().integralSalary());
        assertEquals(RiskLevel.ONE, result.contractFound().riskLevelArl());
        assertEquals(LocalDate.of(2026, 2, 1), result.contractFound().startDate());
        assertEquals(LocalDate.of(2027, 2, 1), result.contractFound().endDate());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        GetContractByIdUseCaseIn request = new GetContractByIdUseCaseIn(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("contract.id.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }
}
