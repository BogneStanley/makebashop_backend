package cm.bognestanley.shop_backend.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cm.bognestanley.shop_backend.presentation.dto.response.common.ResponseDataWrapper;
import cm.bognestanley.shop_backend.presentation.dto.response.dashboard.DashboardResponse;
import cm.bognestanley.shop_backend.presentation.facade.DashboardFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Administration dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardFacade dashboardFacade;

    @GetMapping
    @Operation(summary = "Get dashboard summary")
    public ResponseEntity<ResponseDataWrapper<DashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ResponseDataWrapper.ok(dashboardFacade.getDashboard()));
    }
}
