package com.entic.payroll.core.application.novelty;

import com.entic.payroll.core.application.impl.GetNoveltyByIdUseCaseImpl;
import com.entic.payroll.core.application.models.GetNoveltyByIdUseCaseIn;
import com.entic.payroll.core.application.models.GetNoveltyByIdUseCaseOut;
import com.entic.payroll.core.application.port.out.NoveltyRepositoryPort;
import com.entic.payroll.core.domain.enums.NoveltyType;
import com.entic.payroll.core.domain.errors.ValidationException;
import com.entic.payroll.core.domain.novelty.Novelty;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetNoveltyByIdUseCaseTest {

    private final NoveltyRepositoryPort repositoryPort = mock(NoveltyRepositoryPort.class);
    private final GetNoveltyByIdUseCaseImpl service = new GetNoveltyByIdUseCaseImpl(repositoryPort);

    @Test
    void shouldGetNoveltyByIdSuccessfully() {
        Long id = 5L;
        Long employeeId = 1L;
        GetNoveltyByIdUseCaseIn request = new GetNoveltyByIdUseCaseIn(id);

        Novelty novelty = Novelty.reconstitute(id, employeeId, NoveltyType.MATERNITY_LEAVE,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 7, 1), 30);
        when(repositoryPort.findById(id)).thenReturn(novelty);

        GetNoveltyByIdUseCaseOut result = service.execute(request);

        assertNotNull(result);
        assertNotNull(result.noveltyFound());
        assertEquals(id, result.noveltyFound().id());
        assertEquals(employeeId, result.noveltyFound().employeeId());
        assertEquals(NoveltyType.MATERNITY_LEAVE, result.noveltyFound().noveltyType());
        assertEquals(LocalDate.of(2026, 6, 1), result.noveltyFound().startDate());
        assertEquals(LocalDate.of(2026, 7, 1), result.noveltyFound().endDate());
        assertEquals(30, result.noveltyFound().daysApplied());
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        GetNoveltyByIdUseCaseIn request = new GetNoveltyByIdUseCaseIn(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.execute(request));
        assertEquals("novelty.id.required", ex.getMessage());
        verify(repositoryPort, never()).findById(any());
    }
}
