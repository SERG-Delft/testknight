package com.testknight.TestKnightTelemetryServer.validation;

import com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestKnightTelemetryServer.exceptions.*;
import com.testknight.TestKnightTelemetryServer.repositories.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContentValidatorTest {

    @MockBean
    private ActionRepository actionRepository;

    private UsageDataDto usageDataDto = new UsageDataDto("userId", new ArrayList<>(), "hash");

    private ContentValidator contentValidator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(actionRepository.existsById(anyString())).thenReturn(true);
        contentValidator = new ContentValidator(actionRepository);
    }

    @Test
    public void testGoodWeather() {
        assertDoesNotThrow(() -> {
            contentValidator.handle(usageDataDto);
        });
    }

    @Test
    public void testNullActionsRecorded() {
        usageDataDto.setActionsRecorded(null);
        assertThrows(NullFieldException.class, () -> {
           contentValidator.handle(usageDataDto);
        });
    }

    @Test
    public void testNullUserId() {
        usageDataDto.setUserId(null);
        assertThrows(NullFieldException.class, () -> {
            contentValidator.handle(usageDataDto);
        });
    }

    @Test
    public void testActionDoesNotExist() {
        when(actionRepository.existsById(anyString())).thenReturn(false);
        ArrayList<ActionEventDto> actions = new ArrayList<>();
        actions.add(new ActionEventDto("invalidActionId", LocalDateTime.now()));
        usageDataDto.setActionsRecorded(actions);
        assertThrows(InvalidActionIdException.class, () -> {
            contentValidator.handle(usageDataDto);
        });
    }


}