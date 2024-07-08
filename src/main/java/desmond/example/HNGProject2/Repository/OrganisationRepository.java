package desmond.example.HNGProject2.Repository;

import desmond.example.HNGProject2.model.Organisation;
import desmond.example.HNGProject2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    List<Organisation> findByUsers(User user);
    Organisation findByUsersAndOrgId(User user, Long orgId);

    Object findAllByUsersContains(User testUser);
}