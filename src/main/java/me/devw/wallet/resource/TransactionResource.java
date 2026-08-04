package me.devw.wallet.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.devw.wallet.dto.AccountBalanceResponse;
import me.devw.wallet.dto.AccountStatementRequest;
import me.devw.wallet.dto.CreateTransactionRequest;
import me.devw.wallet.entity.Account;
import me.devw.wallet.entity.AccountType;
import me.devw.wallet.entity.Statement;
import me.devw.wallet.entity.Transaction;
import me.devw.wallet.security.SecurityContextHolder;
import me.devw.wallet.service.AccountService;
import me.devw.wallet.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Path("/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @Inject
    AccountService accountService;

    @Inject
    TransactionService transactionService;

    @GET
    public Response getTransactions(
            @QueryParam("accountId") String accountId,
            @QueryParam("year") Integer year,
            @QueryParam("month") Integer month
    ) {
        SecurityContextHolder.UserPrincipal user = SecurityContextHolder.get();
        System.out.println("accountId = " + accountId);
        System.out.println("year = " + year);
        System.out.println("month = " + month);

        List<Statement> statement = transactionService.getStatement(
                UUID.fromString(user.userId()),
                UUID.fromString(accountId),
                year,
                month
        );











        return Response.ok(statement).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTransaction(CreateTransactionRequest request) {

        SecurityContextHolder.UserPrincipal user = SecurityContextHolder.get();

        System.out.println(user.userId());

        Transaction transaction = new Transaction();
        transaction.setUser_id(UUID.fromString(user.userId()));
        transaction.setDebit_account_id(UUID.fromString(request.debitAccountId));
        transaction.setCredit_account_id(UUID.fromString(request.creditAccountId));
        transaction.setAmount(request.amount);
        transaction.setDescription(request.description);
        transaction.setTransactionDate(request.date);
        transaction.setCreatedAt(LocalDate.now());

       transactionService.save(transaction);


        return Response.status(Response.Status.CREATED).build();
    }
//    @POST
//    public Response createAccount(CreateAccountRequest request,
//                                  @Context ContainerRequestContext requestContext) {
//
//        EntityManager entityManager = JpaUtil.getEntityManager();
//        EntityTransaction tx = entityManager.getTransaction();
//
//        try {
//            tx.begin();
//
//            User user = (User) requestContext.getProperty("authenticatedUser");
//
//            Account account = new Account();
//            account.setUserId(user.getId());
//            account.setName(request.name);
//            account.setAccountType(request.accountType);
//            account.setIcon(request.icon);
//            account.setCreatedAt(LocalDateTime.now());
//
//            entityManager.persist(account);
//
//            tx.commit();
//
//            return Response.status(Response.Status.CREATED)
//                    .entity(account)
//                    .build();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            if (tx.isActive()) tx.rollback();
//
//            return Response.serverError()
//                    .entity(e.getMessage())
//                    .build();
//
//        } finally {
//            entityManager.close();
//        }
//    }
}