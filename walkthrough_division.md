# Project Files Division for 4 Team Members
Below is the detailed list of files under src/main grouped by member (Module) and file types.

---

## Member 1: Module 1 (Authentication & Authorization) + Security & Common
> Responsible for Security, Login, Register, Forget/Reset Password, OTP, Common utils, configs, main application, and layouts.

### Controllers
- `src/main/java/flearn/module/auth/controller/AccountController.java`
- `src/main/java/flearn/module/auth/controller/AuthController.java`
- `src/main/java/flearn/module/auth/controller/HomeController.java`
- `src/main/java/flearn/module/auth/controller/PasswordResetController.java`

### Services (Business Logic)
- `src/main/java/flearn/common/service/EmailService.java`
- `src/main/java/flearn/common/service/impl/EmailServiceImpl.java`
- `src/main/java/flearn/module/auth/service/AuthService.java`
- `src/main/java/flearn/module/auth/service/impl/AuthServiceImpl.java`
- `src/main/java/flearn/module/auth/service/impl/PasswordResetServiceImpl.java`
- `src/main/java/flearn/module/auth/service/impl/UserServiceImpl.java`
- `src/main/java/flearn/module/auth/service/PasswordResetService.java`
- `src/main/java/flearn/module/auth/service/UserService.java`
- `src/main/java/flearn/security/service/CustomUserDetails.java`
- `src/main/java/flearn/security/service/CustomUserDetailsService.java`

### DTOs (Data Transfer Objects)
- `src/main/java/flearn/module/auth/dto/request/ChangePasswordRequest.java`
- `src/main/java/flearn/module/auth/dto/request/RegisterStudentRequest.java`
- `src/main/java/flearn/module/auth/dto/request/UpdateUserRequest.java`
- `src/main/java/flearn/module/auth/dto/response/UserResponse.java`

### Mappers
- `src/main/java/flearn/module/auth/mapper/UserMapper.java`

### Database (Entities, Repositories, Enums)
- `src/main/java/flearn/entity/User.java`
- `src/main/java/flearn/enums/QuestionType.java`
- `src/main/java/flearn/enums/Role.java`
- `src/main/java/flearn/enums/UserStatus.java`
- `src/main/java/flearn/repository/UserRepository.java`

### Templates & Static UI Files
- `src/main/resources/static/css/app.css`
- `src/main/resources/templates/auth/change-password.html`
- `src/main/resources/templates/auth/forgot-password.html`
- `src/main/resources/templates/auth/login.html`
- `src/main/resources/templates/auth/register.html`
- `src/main/resources/templates/auth/reset-password.html`
- `src/main/resources/templates/auth/verify-otp.html`
- `src/main/resources/templates/error/access-denied.html`
- `src/main/resources/templates/error/maintenance.html`
- `src/main/resources/templates/fragments/alerts.html`
- `src/main/resources/templates/fragments/footer.html`
- `src/main/resources/templates/fragments/head.html`
- `src/main/resources/templates/fragments/scripts.html`
- `src/main/resources/templates/fragments/sidebar.html`
- `src/main/resources/templates/fragments/topbar.html`

### Configurations & Properties
- `src/main/java/flearn/common/config/ApplicationInitConfig.java`
- `src/main/java/flearn/common/config/BrowserLauncher.java`
- `src/main/java/flearn/common/config/MaintenanceInterceptor.java`
- `src/main/java/flearn/common/config/WebMvcConfig.java`
- `src/main/java/flearn/common/exception/BusinessException.java`
- `src/main/java/flearn/common/exception/GlobalExceptionHandler.java`
- `src/main/java/flearn/common/util/EmbedUrlUtil.java`
- `src/main/java/flearn/common/validation/ValidationMessage.java`
- `src/main/java/flearn/FLearnSpringBootApplication.java`
- `src/main/java/flearn/security/config/SecurityConfig.java`
- `src/main/resources/application.yaml`


---

## Member 2: Module 2 (Class, User & System Management)
> Responsible for Courses, Classrooms, Students, Lecturers, Enrollments, System Settings, Admin Dashboard.

### Controllers
- `src/main/java/flearn/module/management/controller/AdminClassController.java`
- `src/main/java/flearn/module/management/controller/AdminCourseController.java`
- `src/main/java/flearn/module/management/controller/AdminDashboardController.java`
- `src/main/java/flearn/module/management/controller/AdminLecturerController.java`
- `src/main/java/flearn/module/management/controller/AdminStudentController.java`
- `src/main/java/flearn/module/management/controller/AdminSystemController.java`
- `src/main/java/flearn/module/management/controller/StudentEnrollmentController.java`
- `src/main/java/flearn/module/management/controller/TeacherEnrollmentController.java`

### Services (Business Logic)
- `src/main/java/flearn/module/management/service/ClassroomService.java`
- `src/main/java/flearn/module/management/service/CourseService.java`
- `src/main/java/flearn/module/management/service/EnrollmentService.java`
- `src/main/java/flearn/module/management/service/impl/ClassroomServiceImpl.java`
- `src/main/java/flearn/module/management/service/impl/CourseServiceImpl.java`
- `src/main/java/flearn/module/management/service/impl/EnrollmentServiceImpl.java`
- `src/main/java/flearn/module/management/service/impl/StudentServiceImpl.java`
- `src/main/java/flearn/module/management/service/impl/SystemSettingServiceImpl.java`
- `src/main/java/flearn/module/management/service/StudentService.java`
- `src/main/java/flearn/module/management/service/SystemSettingService.java`

### DTOs (Data Transfer Objects)
- `src/main/java/flearn/module/management/dto/request/AssignTeacherRequest.java`
- `src/main/java/flearn/module/management/dto/request/ClassroomRequest.java`
- `src/main/java/flearn/module/management/dto/request/CourseRequest.java`
- `src/main/java/flearn/module/management/dto/request/CreateTeacherRequest.java`
- `src/main/java/flearn/module/management/dto/request/JoinClassRequest.java`
- `src/main/java/flearn/module/management/dto/request/RejectEnrollmentRequest.java`
- `src/main/java/flearn/module/management/dto/response/AdminStatisticsResponse.java`
- `src/main/java/flearn/module/management/dto/response/ClassroomResponse.java`
- `src/main/java/flearn/module/management/dto/response/CourseResponse.java`
- `src/main/java/flearn/module/management/dto/response/EnrollmentResponse.java`

### Mappers
- `src/main/java/flearn/module/management/mapper/ClassroomMapper.java`
- `src/main/java/flearn/module/management/mapper/CourseMapper.java`
- `src/main/java/flearn/module/management/mapper/EnrollmentMapper.java`

### Database (Entities, Repositories, Enums)
- `src/main/java/flearn/entity/Classroom.java`
- `src/main/java/flearn/entity/Course.java`
- `src/main/java/flearn/entity/Enrollment.java`
- `src/main/java/flearn/entity/SystemSetting.java`
- `src/main/java/flearn/enums/ClassStatus.java`
- `src/main/java/flearn/enums/CourseStatus.java`
- `src/main/java/flearn/enums/EnrollmentStatus.java`
- `src/main/java/flearn/repository/ClassroomRepository.java`
- `src/main/java/flearn/repository/CourseRepository.java`
- `src/main/java/flearn/repository/EnrollmentRepository.java`
- `src/main/java/flearn/repository/SystemSettingRepository.java`

### Templates & Static UI Files
- `src/main/resources/templates/admin/classes/create.html`
- `src/main/resources/templates/admin/classes/detail.html`
- `src/main/resources/templates/admin/classes/edit.html`
- `src/main/resources/templates/admin/classes/list.html`
- `src/main/resources/templates/admin/courses/create.html`
- `src/main/resources/templates/admin/courses/detail.html`
- `src/main/resources/templates/admin/courses/edit.html`
- `src/main/resources/templates/admin/courses/list.html`
- `src/main/resources/templates/admin/dashboard.html`
- `src/main/resources/templates/admin/lecturers/list.html`
- `src/main/resources/templates/admin/statistics.html`
- `src/main/resources/templates/admin/students/list.html`
- `src/main/resources/templates/admin/system/settings.html`
- `src/main/resources/templates/student/classes/join.html`
- `src/main/resources/templates/student/classes/list.html`
- `src/main/resources/templates/teacher/classes/enrollments.html`
- `src/main/resources/templates/teacher/classes/list.html`


---

## Member 3: Module 3 (Learning Content Management)
> Responsible for Roadmaps, Lessons, Materials, Material Tracking.

### Controllers
- `src/main/java/flearn/module/content/controller/StudentClassController.java`
- `src/main/java/flearn/module/content/controller/TeacherClassController.java`

### Services (Business Logic)
- `src/main/java/flearn/module/content/service/impl/LearningContentServiceImpl.java`
- `src/main/java/flearn/module/content/service/LearningContentService.java`

### DTOs (Data Transfer Objects)
- `src/main/java/flearn/module/content/dto/request/LearningLessonRequest.java`
- `src/main/java/flearn/module/content/dto/request/MaterialRequest.java`
- `src/main/java/flearn/module/content/dto/request/RoadmapRequest.java`
- `src/main/java/flearn/module/content/dto/response/LessonResponse.java`
- `src/main/java/flearn/module/content/dto/response/MaterialResponse.java`
- `src/main/java/flearn/module/content/dto/response/MaterialTrackingResponse.java`
- `src/main/java/flearn/module/content/dto/response/RoadmapResponse.java`

### Mappers
- `src/main/java/flearn/module/content/mapper/LessonMapper.java`
- `src/main/java/flearn/module/content/mapper/MaterialMapper.java`
- `src/main/java/flearn/module/content/mapper/MaterialTrackingMapper.java`
- `src/main/java/flearn/module/content/mapper/RoadmapMapper.java`

### Database (Entities, Repositories, Enums)
- `src/main/java/flearn/entity/Lesson.java`
- `src/main/java/flearn/entity/Material.java`
- `src/main/java/flearn/entity/MaterialTracking.java`
- `src/main/java/flearn/entity/Roadmap.java`
- `src/main/java/flearn/enums/MaterialType.java`
- `src/main/java/flearn/repository/LessonRepository.java`
- `src/main/java/flearn/repository/MaterialRepository.java`
- `src/main/java/flearn/repository/MaterialTrackingRepository.java`
- `src/main/java/flearn/repository/RoadmapRepository.java`

### Templates & Static UI Files
- `src/main/resources/templates/student/classes/learning.html`
- `src/main/resources/templates/student/classes/learning-history.html`
- `src/main/resources/templates/student/classes/lesson-learning.html`
- `src/main/resources/templates/student/classes/material-detail.html`
- `src/main/resources/templates/teacher/classes/detail.html`
- `src/main/resources/templates/teacher/classes/lesson-form.html`
- `src/main/resources/templates/teacher/classes/material-form.html`
- `src/main/resources/templates/teacher/classes/roadmap-form.html`
- `src/main/resources/templates/teacher/classes/roadmaps.html`


---

## Member 4: Module 4 (Quiz & Assessment Management)
> Responsible for Quizzes, Questions, Answer Options, Take Quiz, History and Results.

### Controllers
- `src/main/java/flearn/module/quiz/controller/StudentQuizController.java`
- `src/main/java/flearn/module/quiz/controller/TeacherQuizController.java`

### Services (Business Logic)
- `src/main/java/flearn/module/quiz/service/impl/QuizServiceImpl.java`
- `src/main/java/flearn/module/quiz/service/QuizService.java`

### DTOs (Data Transfer Objects)
- `src/main/java/flearn/module/quiz/dto/request/CreateQuizRequest.java`
- `src/main/java/flearn/module/quiz/dto/request/QuestionRequest.java`
- `src/main/java/flearn/module/quiz/dto/request/QuizRequest.java`
- `src/main/java/flearn/module/quiz/dto/response/AnswerOptionResponse.java`
- `src/main/java/flearn/module/quiz/dto/response/QuestionResponse.java`
- `src/main/java/flearn/module/quiz/dto/response/QuizResponse.java`
- `src/main/java/flearn/module/quiz/dto/response/QuizResultResponse.java`

### Mappers
- `src/main/java/flearn/module/quiz/mapper/QuestionMapper.java`
- `src/main/java/flearn/module/quiz/mapper/QuizMapper.java`
- `src/main/java/flearn/module/quiz/mapper/QuizResultMapper.java`

### Database (Entities, Repositories, Enums)
- `src/main/java/flearn/entity/Question.java`
- `src/main/java/flearn/entity/Quiz.java`
- `src/main/java/flearn/entity/QuizResult.java`
- `src/main/java/flearn/enums/QuizSubmissionStatus.java`
- `src/main/java/flearn/repository/QuestionRepository.java`
- `src/main/java/flearn/repository/QuizRepository.java`
- `src/main/java/flearn/repository/QuizResultRepository.java`

### Templates & Static UI Files
- `src/main/resources/templates/student/quizzes/history.html`
- `src/main/resources/templates/student/quizzes/results.html`
- `src/main/resources/templates/student/quizzes/take.html`
- `src/main/resources/templates/teacher/quizzes/form.html`
- `src/main/resources/templates/teacher/quizzes/list.html`
- `src/main/resources/templates/teacher/quizzes/question-form.html`
- `src/main/resources/templates/teacher/quizzes/results.html`


---

