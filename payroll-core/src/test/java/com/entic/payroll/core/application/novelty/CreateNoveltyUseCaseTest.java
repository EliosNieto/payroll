package com.entic.payroll.core.application.novelty;

import com.entic.payroll.core.application.impl.CreateNoveltyUseCaseImpl;
import com.entic.payroll.core.application.models.CreateNoveltyUseCaseIn;
import com.entic.payroll.core.application.models.CreateNoveltyUseCaseOut;
import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.core.domain.enums.NoveltyType;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.novelty.Novelty;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateNoveltyUseCaseTest {

    private final NoveltyRepositoryPort repositoryPort = mock(NoveltyRepositoryPort.class);
    private final CreateNoveltyUseCaseImpl service = new CreateNoveltyUseCaseImpl(repositoryPort);

    private static final Long EMPLOYEE_ID = 1L;

    private CreateNoveltyUseCaseIn validCommand() {
        return new CreateNoveltyUseCaseIn(
                EMPLOYEE_ID,
                NoveltyType.DISABILITY,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 15),
                15
        );
    }

    @Test
    void shouldCreateNoveltySuccessfully() {
        CreateNoveltyUseCaseIn command = validCommand();

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);
        when(repositoryPort.save(any(Novelty.class))).thenAnswer(invocation -> {
            Novelty n = invocation.getArgument(0);
            return Novelty.reconstitute(1L, n.getEmployeeId(), n.getNoveltyType(),
                    n.getStartDate(), n.getEndDate(), n.getDaysApplied());
        });

        CreateNoveltyUseCaseOut result = service.execute(command);

        assertNotNull(result);
        assertNotNull(result.noveltyCreated());
        assertNotNull(result.noveltyCreated().id());
        assertEquals(EMPLOYEE_ID, result.noveltyCreated().employeeId());
        assertEquals(NoveltyType.DISABILITY, result.noveltyCreated().noveltyType());
        assertEquals(LocalDate.of(2026, 5, 1), result.noveltyCreated().startDate());
        assertEquals(LocalDate.of(2026, 5, 15), result.noveltyCreated().endDate());
        assertEquals(15, result.noveltyCreated().daysApplied());
        verify(repositoryPort).save(any(Novelty.class));
    }

    @Test
    void shouldFailWhenEmployeeIdIsNull() {
        CreateNoveltyUseCaseIn command = new CreateNoveltyUseCaseIn(null,
                NoveltyType.DISABILITY, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), 15);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("novelty.employeeId.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEmployeeNotFound() {
        CreateNoveltyUseCaseIn command = validCommand();

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(command));
        assertEquals("novelty.employee.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNoveltyTypeIsNull() {
        CreateNoveltyUseCaseIn command = new CreateNoveltyUseCaseIn(EMPLOYEE_ID,
                null, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), 15);

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("novelty.noveltyType.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenStartDateIsNull() {
        CreateNoveltyUseCaseIn command = new CreateNoveltyUseCaseIn(EMPLOYEE_ID,
                NoveltyType.DISABILITY, null, LocalDate.of(2026, 5, 15), 15);

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("novelty.startDate.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEndDateIsNotAfterStartDate() {
        CreateNoveltyUseCaseIn command = new CreateNoveltyUseCaseIn(EMPLOYEE_ID,
                NoveltyType.DISABILITY, LocalDate.of(2026, 5, 15), LocalDate.of(2026, 5, 1), 15);

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("novelty.dateRange.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDaysAppliedIsZero() {
        CreateNoveltyUseCaseIn command = new CreateNoveltyUseCaseIn(EMPLOYEE_ID,
                NoveltyType.DISABILITY, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), 0);

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("novelty.daysApplied.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDaysAppliedIsNegative() {
        CreateNoveltyUseCaseIn command = new CreateNoveltyUseCaseIn(EMPLOYEE_ID,
                NoveltyType.DISABILITY, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 15), -3);

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(command));
        assertEquals("novelty.daysApplied.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldAllowNoveltyWithNullEndDate() {
        CreateNoveltyUseCaseIn command = new CreateNoveltyUseCaseIn(EMPLOYEE_ID,
                NoveltyType.UNPAID_LEAVE, LocalDate.of(2026, 5, 1), null, 5);

        when(repositoryPort.employeeExists(EMPLOYEE_ID)).thenReturn(true);
        when(repositoryPort.save(any(Novelty.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateNoveltyUseCaseOut result = service.execute(command);

        assertNotNull(result);
        assertNull(result.noveltyCreated().endDate());
        assertEquals(5, result.noveltyCreated().daysApplied());
        verify(repositoryPort).save(any(Novelty.class));
    }
}
