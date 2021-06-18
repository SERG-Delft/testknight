package com.testknight.TestKnightTelemetryServer.validation;

import com.testknight.TestKnightTelemetryServer.dataTransferObjects.requests.*;
import com.testknight.TestKnightTelemetryServer.exceptions.*;
import com.testknight.TestKnightTelemetryServer.repositories.*;

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
