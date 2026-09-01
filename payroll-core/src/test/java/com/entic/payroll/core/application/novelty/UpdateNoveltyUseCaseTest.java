package com.entic.payroll.core.application.novelty;

import com.entic.payroll.core.application.impl.UpdateNoveltyUseCaseImpl;
import com.entic.payroll.core.application.models.UpdateNoveltyUseCaseIn;
import com.entic.payroll.core.application.models.UpdateNoveltyUseCaseOut;
import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.core.domain.enums.NoveltyType;
import com.entic.payroll.core.domain.errors.NotFoundException;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.novelty.Novelty;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateNoveltyUseCaseTest {

    private final NoveltyRepositoryPort repositoryPort = mock(NoveltyRepositoryPort.class);
    private final UpdateNoveltyUseCaseImpl service = new UpdateNoveltyUseCaseImpl(repositoryPort);

    private static final Long ID = 5L;
    private static final Long EMPLOYEE_ID = 1L;

    private UpdateNoveltyUseCaseIn validCommand() {
        return new UpdateNoveltyUseCaseIn(
                ID,
                NoveltyType.PATERNITY_LEAVE,
                LocalDate.of(2026, 6, 10),
                LocalDate.of(2026, 6, 18),
                8
        );
    }

    @Test
    void shouldUpdateNoveltySuccessfully() {
        UpdateNoveltyUseCaseIn request = validCommand();

        Novelty existing = Novelty.reconstitute(ID, EMPLOYEE_ID, NoveltyType.PATERNITY_LEAVE,
                LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 18), 8);
        when(repositoryPort.findById(ID)).thenReturn(existing);
        when(repositoryPort.save(any(Novelty.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateNoveltyUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.noveltyUpdated());
        assertEquals(ID, result.noveltyUpdated().id());
        assertEquals(EMPLOYEE_ID, result.noveltyUpdated().employeeId());
        assertEquals(NoveltyType.PATERNITY_LEAVE, result.noveltyUpdated().noveltyType());
        assertEquals(LocalDate.of(2026, 6, 10), result.noveltyUpdated().startDate());
        assertEquals(LocalDate.of(2026, 6, 18), result.noveltyUpdated().endDate());
        assertEquals(8, result.noveltyUpdated().daysApplied());
        verify(repositoryPort).save(any(Novelty.class));
    }

    @Test
    void shouldFailWhenIdIsNull() {
        UpdateNoveltyUseCaseIn request = new UpdateNoveltyUseCaseIn(null,
                NoveltyType.PATERNITY_LEAVE, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 18), 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("novelty.id.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNoveltyNotFound() {
        UpdateNoveltyUseCaseIn request = validCommand();

        when(repositoryPort.findById(ID)).thenThrow(new NotFoundException("id", ID.toString(), "novelty.notFound"));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.execute(request));
        assertEquals("novelty.notFound", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenNoveltyTypeIsNull() {
        UpdateNoveltyUseCaseIn request = new UpdateNoveltyUseCaseIn(ID,
                null, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 18), 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("novelty.noveltyType.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenStartDateIsNull() {
        UpdateNoveltyUseCaseIn request = new UpdateNoveltyUseCaseIn(ID,
                NoveltyType.PATERNITY_LEAVE, null, LocalDate.of(2026, 6, 18), 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("novelty.startDate.required", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenEndDateIsNotAfterStartDate() {
        UpdateNoveltyUseCaseIn request = new UpdateNoveltyUseCaseIn(ID,
                NoveltyType.PATERNITY_LEAVE, LocalDate.of(2026, 6, 18), LocalDate.of(2026, 6, 10), 8);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("novelty.dateRange.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldFailWhenDaysAppliedIsZeroOrNegative() {
        UpdateNoveltyUseCaseIn request = new UpdateNoveltyUseCaseIn(ID,
                NoveltyType.PATERNITY_LEAVE, LocalDate.of(2026, 6, 10), LocalDate.of(2026, 6, 18), 0);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("novelty.daysApplied.invalid", ex.getMessage());
        verify(repositoryPort, never()).save(any());
    }
}
