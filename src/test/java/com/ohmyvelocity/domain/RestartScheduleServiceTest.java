package com.ohmyvelocity.domain;

import com.ohmyvelocity.adapter.runtime.RestartScheduleService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestartScheduleServiceTest {
    @TempDir
    Path tempDir;

    @Test
    void warnsOncePerThreshold() throws Exception {
        RestartScheduleService service = RestartScheduleServiceTestSupport.create(tempDir, true, List.of(5, 1));
        long now = System.currentTimeMillis();
        service.scheduleInitialIfNeeded(now);
        long next = service.nextRestartEpochMs();
        long fourMinutesBefore = next - (4 * 60_000L) - 30_000L;

        RestartTickResult first = service.tick(fourMinutesBefore);
        assertEquals(RestartTickResult.Type.WARNING, first.type());
        RestartTickResult duplicate = service.tick(fourMinutesBefore);
        assertEquals(RestartTickResult.Type.IDLE, duplicate.type());
    }

    @Test
    void disabledRestartStaysIdle() throws Exception {
        RestartScheduleService service = RestartScheduleServiceTestSupport.create(tempDir, false, List.of(5));
        service.scheduleInitialIfNeeded(System.currentTimeMillis());
        assertEquals(RestartTickResult.Type.IDLE, service.tick(System.currentTimeMillis()).type());
        assertEquals(0L, service.nextRestartEpochMs());
    }
}
