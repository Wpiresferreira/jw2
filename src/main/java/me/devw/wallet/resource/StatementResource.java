package me.devw.wallet.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.devw.wallet.dto.StatementResponse;
import me.devw.wallet.entity.Account;
import me.devw.wallet.entity.Statement;
import me.devw.wallet.security.SecurityContextHolder;
import me.devw.wallet.service.AccountService;
import me.devw.wallet.service.TransactionService;
import java.util.List;
import java.util.UUID;
import jakarta.ws.rs.*;

@Path("/statement")                     // 🔥 AGORA SIM!
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatementResource {

    @Inject
    AccountService accountService;

    @Inject
    TransactionService transactionService;

    @GET
    @Path("/{id}")                       // 🔥 FICA /statement/{id}
    public Response getStatement(
            @PathParam("id") UUID accountId,
            @QueryParam("year") int year,
            @QueryParam("month") int month
    ) {
        SecurityContextHolder.UserPrincipal user = SecurityContextHolder.get();

        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        UUID userId = UUID.fromString(user.userId());

        // Verifica se a conta pertence ao usuário
        Account account = accountService.findById(accountId);
        if (account == null || !account.getUserId().equals(userId)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Account does not belong to authenticated user")
                    .build();
        }

        // Busca o extrato
        List<Statement> items = transactionService.getStatement(
                userId,
                accountId,
                year,
                month
        );

        StatementResponse response = new StatementResponse(
                accountId,
                year,
                month,
                items
        );

        return Response.ok(response).build();
    }


    @POST
    @Path("/new")
    public Response addNewRecord (){
    return Response.ok().build();
    }

}
