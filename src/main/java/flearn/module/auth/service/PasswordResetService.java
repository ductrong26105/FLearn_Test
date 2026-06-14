package flearn.module.auth.service;

import flearn.entity.User;

public interface PasswordResetService {
    // OTP flow
    void sendOtp(String email);
    User verifyOtp(String email, String otpCode);
    void resetPasswordWithOtp(String email, String otpCode, String newPassword);

    // Token flow (giữ để tương thích ngược)
    void processForgotPassword(String email);
    User getByResetToken(String token);
    void updatePassword(User user, String newPassword);
}
