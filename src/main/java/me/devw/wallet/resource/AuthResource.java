package me.devw.wallet.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import me.devw.wallet.dto.LoginRequest;
import me.devw.wallet.dto.LoginResponse;
import me.devw.wallet.dto.SignupRequest;
import me.devw.wallet.entity.Account;
import me.devw.wallet.entity.AccountType;
import me.devw.wallet.entity.Role;
import me.devw.wallet.entity.User;
import me.devw.wallet.security.JwtService;
import me.devw.wallet.security.PasswordService;
import me.devw.wallet.service.AccountService;
import me.devw.wallet.service.AuthenticationService;
import me.devw.wallet.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AuthenticationService authService;

    @Inject
    private JwtService jwtService;
    @Inject
    private PasswordService passwordService;
    @Inject
    private UserService userService;
    @Inject
    private AccountService accountService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {

        var user = authService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        if (user == null) {
            return Response.status(401).build();
        }

        String token = jwtService.generate(user);

        NewCookie cookie = new NewCookie.Builder("access_token")
                .value(token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60*60*24*365).sameSite(NewCookie.SameSite.NONE)
                .build();


        return Response.ok()
                .cookie(cookie)
                .build();
    }

    @POST
    @Path("/logout")
    public Response logout() {

        NewCookie cookie = new NewCookie.Builder("access_token")
                .value("")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return Response.ok()
                .cookie(cookie)
                .build();
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(SignupRequest request) {


        boolean exists = userService.existsByUsername(
                request.getUsername()
        );

        if (exists) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Username already exists")
                    .build();
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setId(UUID.randomUUID());


        user.setPassword(
                passwordService.hash(request.getPassword())
        );
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        userService.save(user);

        // Create the Opening Equity Account
        Account account = new Account();
        account.setName("Opening Equity");
        account.setUserId(user.getId());
        account.setAccountType(AccountType.EQUITY);
        account.setIcon("MdAttachMoney");
        account.setCurrency("USD");
        account.setOpeningDate(LocalDate.of(1970, 1, 1));
        account.setCreatedAt(LocalDate.now());

        accountService.save(account);

        return Response.status(Response.Status.CREATED)
                .build();
    }
}