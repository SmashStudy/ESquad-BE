package com.esquad.esquadbe.global.temp;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "테스트", description = "테스트 관련 API")
@RestController
public class SwaggerTestController {

    @Operation(
        summary = "테스트 메서드",
        description = "테스트 메서드입니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "테스트 성공함."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "테스트 실패함."
            )
        }
    )
    @GetMapping("/test/{no}")
    public ResponseEntity<TestViewResponse> getTestData(
        @Parameter(description = "테스트할 숫자", example = "testNo")
        @PathVariable(name = "no") int no) {

        if (no <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        TestViewResponse response = new TestViewResponse();
        response.setTestNo(no);
        response.setTestBody("성공");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
