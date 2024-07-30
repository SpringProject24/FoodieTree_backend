package org.nmfw.foodietree.global.error;

import io.swagger.models.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice // 전역 설정을 위한 annotation
@RestController
public class ExceptionAdvisor {

    /**
     * 모든 컨트롤러에서 발생하는 Validation 관련 Exception 처리
     * @param exception - MethodArgumentNotValidException : 스프링 Validation 모듈 관련 Exception
     * @return - ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> makeValidationErrorResponse(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, String> errors = makeValidationMessageMap(bindingResult);

        return ResponseEntity.badRequest().body(errors);
    }


    private Map<String, String> makeValidationMessageMap(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error: fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
