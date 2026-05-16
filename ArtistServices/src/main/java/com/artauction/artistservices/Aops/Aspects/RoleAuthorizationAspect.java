package com.artauction.artistservices.Aops.Aspects;

import com.artauction.artistservices.Aops.Roles.RequiresRole;
import com.artauction.artistservices.Security.JwtRoleExtractor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAuthorizationAspect {
    // Attributes
    private final JwtRoleExtractor jwtRoleExtractor;

    // Intercept any method or class annotated with @RequiresRole
    @Around("@annotation(requiresRole) || @within(requiresRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequiresRole requiresRole) throws Throwable {
        // Resolve which @RequiresRole to use:
        // Method-level annotation takes priority over class-level
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // Extract allowed roles from annotation
        String[] allowedRoles = requiresRole.value();

        // Get current HTTP request
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: No request context found.");

        HttpServletRequest request = attributes.getRequest();

        // Extract Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: Missing or invalid Authorization header.");

        // Extract token
        String token = authHeader.substring(7);

        // Extract roles from JWT token
        String userRole;
        try {
            userRole = jwtRoleExtractor.extractRole(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: Invalid or expired token.");
        }

        // Check if user role matches any allowed role
        boolean hasAccess = Arrays.asList(allowedRoles).contains(userRole);
        if (!hasAccess)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Forbidden: You do not have permission to access this resource.");

        // Role matched — proceed to actual method
        return joinPoint.proceed();
    }
}
