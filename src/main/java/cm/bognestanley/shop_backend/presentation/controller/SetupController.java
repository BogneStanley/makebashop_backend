package cm.bognestanley.shop_backend.presentation.controller;

import cm.bognestanley.shop_backend.presentation.dto.request.setup.InitialAdminSetupRequest;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ErrorDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.setup.SetupStatusResponse;
import cm.bognestanley.shop_backend.presentation.dto.response.user.UserResponse;
import cm.bognestanley.shop_backend.presentation.facade.SetupFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/setup")
@Tag(name = "Initial setup", description = "One-time first administrator setup")
public class SetupController {

    private final SetupFacade setupFacade;

    public SetupController(SetupFacade setupFacade) {
        this.setupFacade = setupFacade;
    }

    @GetMapping
    @Operation(summary = "Check whether initial setup is available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Initial setup is available"),
            @ApiResponse(responseCode = "404", description = "Initial setup is no longer available", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<SetupStatusResponse>> getStatus() {
        return ResponseEntity.ok(ResponseDataWrapper.ok(
                setupFacade.getStatus(), "SETUP_REQUIRED", "Initial setup is available"));
    }

    @PostMapping
    @Operation(summary = "Create the first administrator exactly once")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "First administrator created"),
            @ApiResponse(responseCode = "400", description = "Invalid setup request", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Initial setup is no longer available", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(schema = @Schema(implementation = ErrorDataWrapper.class)))
    })
    public ResponseEntity<ResponseDataWrapper<UserResponse>> complete(
            @Valid @RequestBody InitialAdminSetupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDataWrapper.ok(
                setupFacade.complete(request), "INITIAL_ADMIN_CREATED", "Initial administrator created"));
    }
}
