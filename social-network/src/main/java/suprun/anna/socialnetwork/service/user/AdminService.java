package suprun.anna.socialnetwork.service.user;

public interface AdminService {
    void banUser(Long userId, String reason);

    boolean checkIfBannedById(Long userId);

    void unbanUser(Long userId);
}
