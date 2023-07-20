package com.example.demowithtests.util.exception;

import com.example.demowithtests.util.exception.employee.EmployeeContainsException;
import com.example.demowithtests.util.exception.employee.ResourceNotFoundException;
import com.example.demowithtests.util.exception.employee.ResourceWasDeletedException;
import com.example.demowithtests.util.exception.passport.PassportAlreadyPhotoException;
import com.example.demowithtests.util.exception.passport.PassportIsHandedException;
import com.example.demowithtests.util.exception.passport.PassportNoOneFindException;
import com.example.demowithtests.util.exception.passport.PassportNotFoundException;
import com.example.demowithtests.util.exception.photo.PhotoNoOneFindException;
import com.example.demowithtests.util.exception.photo.PhotoNotFoundException;
import com.example.demowithtests.util.exception.reservation.ReservationNotFoundException;
import com.example.demowithtests.util.exception.workplace.WorkPlaceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final static String EMPLOYEE_NOT_FOUND = "Employee not found with id = ";
    private final static String EMPLOYEE_WAS_DELETED = "Employee was deleted with id = ";
    private final static String EMPLOYEE_CONTAINS = "Employee already exists!";
    private final static String PASSPORT_NOT_FOUND_WITH_ID = "Passport not found with id = ";
    private final static String PASSPORT_IS_HANDED = "This passport is handed another employee!";
    private final static String PASSPORT_ALREADY_PHOTO = "This passport already has a photo!";
    private final static String PASSPORT_NO_ONE_FIND = "No one passport find in database!";
    private final static String PHOTO_NO_ONE_FIND = "No one photo find in database!";
    private final static String PHOTO_NOT_FOUND_WITH_ID = "Photo not found in database!";
    private final static String WORK_PLACE_NOT_FOUND = "Work place not fount!";
    private final static String RESERVATION_NOT_FOUND = "Reservation place not fount!";

    /**
     * @apiNote Exceptions methods to Employee entity.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        EMPLOYEE_NOT_FOUND + getIdFromWebRequest(request),
                        request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceWasDeletedException.class)
    protected ResponseEntity<?> resourceWasDeletedException(WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        EMPLOYEE_WAS_DELETED + getIdFromWebRequest(request),
                        request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeContainsException.class)
    protected ResponseEntity<?> employeeContainsException(WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        EMPLOYEE_CONTAINS,
                        request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorDetails errorDetails =
                new ErrorDetails(new Date(),
                        ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @apiNote Exceptions methods to Passport entity.
     */
    @ExceptionHandler(PassportNotFoundException.class)
    protected ResponseEntity<?> passportNotFound(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                PASSPORT_NOT_FOUND_WITH_ID + getIdFromWebRequest(request),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PassportIsHandedException.class)
    protected ResponseEntity<?> passportIsHanded(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                PASSPORT_IS_HANDED,
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PassportAlreadyPhotoException.class)
    protected ResponseEntity<?> passportAlreadyPhoto(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), PASSPORT_ALREADY_PHOTO,
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PassportNoOneFindException.class)
    protected ResponseEntity<?> noOneFindPassport(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), PASSPORT_NO_ONE_FIND,
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * @apiNote Exceptions methods to Photo entity.
     */
    @ExceptionHandler(PhotoNoOneFindException.class)
    protected ResponseEntity<?> noOneFindPhoto(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), PHOTO_NO_ONE_FIND,
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhotoNotFoundException.class)
    protected ResponseEntity<?> photoNotFound(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                PHOTO_NOT_FOUND_WITH_ID, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * @apiNote Exceptions methods to WorkPlace entity.
     */
    @ExceptionHandler(WorkPlaceNotFoundException.class)
    protected ResponseEntity<?> workPlaceNotFound(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                WORK_PLACE_NOT_FOUND, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * @apiNote Exceptions methods to Reservation entity.
     */
    @ExceptionHandler(ReservationNotFoundException.class)
    protected ResponseEntity<?> reservationNotFound(WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                RESERVATION_NOT_FOUND, request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * @apiNote Inside methods
     */
    private static String getIdFromWebRequest(WebRequest request) {
        String path = request.getDescription(false);
        String[] pathSegments = path.split("/");
        return pathSegments[pathSegments.length - 1];
    }
}
