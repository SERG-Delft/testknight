package com.testknight.TestBuddyTelemetryServer.validation;

import com.testknight.TestBuddyTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestBuddyTelemetryServer.exceptions.*;
import com.testknight.TestBuddyTelemetryServer.repositories.*;

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
        if (requestDto.getActionsRecorded() == null) {
            throw new NullFieldException("actionsRecorded");
        }
        if (requestDto.getUserId() == null) {
            throw new NullFieldException("userId");
        }
        for (ActionEventDto actionEventDto : requestDto.getActionsRecorded()) {
            if (!actionRepository.existsById(actionEventDto.getActionId())) {
                throw new InvalidActionIdException(actionEventDto.getActionId());
            }
        }
        super.handle(requestDto);
    }
}
