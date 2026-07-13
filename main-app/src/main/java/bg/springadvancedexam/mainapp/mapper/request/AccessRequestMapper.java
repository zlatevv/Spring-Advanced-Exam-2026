package bg.springadvancedexam.mainapp.mapper.request;

import bg.springadvancedexam.mainapp.dto.request.AccessRequestCreateRequest;
import bg.springadvancedexam.mainapp.dto.request.AccessRequestResponse;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.RequestStatus;

import java.time.LocalDateTime;

public class AccessRequestMapper {
    public static AccessRequest toEntity(AccessRequestCreateRequest accessRequestCreateRequest,
                                         Manuscript manuscript, User researcher) {
        AccessRequest accessRequest = new AccessRequest();

        accessRequest.setManuscript(manuscript);
        accessRequest.setResearcher(researcher);
        accessRequest.setRequestedDate(LocalDateTime.now());
        accessRequest.setRequestStatus(RequestStatus.PENDING);
        accessRequest.setPurpose(accessRequestCreateRequest.purpose());

        return accessRequest;
    }

    public static AccessRequestResponse toResponse(AccessRequest accessRequest) {
        return new AccessRequestResponse(
                accessRequest.getManuscript().getId(),
                accessRequest.getManuscript().getTitle(),
                accessRequest.getResearcher().getFullName(),
                accessRequest.getPurpose(),
                accessRequest.getRequestStatus(),
                accessRequest.getRequestedDate()
        );
    }
}
