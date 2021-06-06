package com.testbuddy.TestBuddyTelemetryServer.validation;

import com.testbuddy.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testbuddy.TestBuddyTelemetryServer.exceptions.*;
import com.testbuddy.TestBuddyTelemetryServer.repositories.*;

public class ContentValidator extends BaseValidator<UsageDataDto> {

    private ActionRepository actionRepository;

    public ContentValidator(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    /**
     * Handles the request.
     *
     * @param requestDto the request to be handled.
     * @throws ValidationException thrown iff the request is invalid.
     */
    @Override
    public void handle(UsageDataDto requestDto) throws ValidationException {
        for (ActionEventDto actionEventDto : requestDto.getActionsRecorded()) {
            if (!actionRepository.existsById(actionEventDto.getActionId())) {
                throw new InvalidActionIdException(actionEventDto.getActionId());
            }
        }
        super.handle(requestDto);
    }
}
