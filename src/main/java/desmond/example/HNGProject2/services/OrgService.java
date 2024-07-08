package desmond.example.HNGProject2.services;

import desmond.example.HNGProject2.dto.OrgRequest;
import desmond.example.HNGProject2.exception.ErrorResponse;
import desmond.example.HNGProject2.exception.SuccessResponse;

public interface OrgService {

    SuccessResponse getOrganisations();

    SuccessResponse getUserOrganisation(Long orgId);

    Object createOrganisationByUser(OrgRequest orgRequest) throws ErrorResponse;

}
