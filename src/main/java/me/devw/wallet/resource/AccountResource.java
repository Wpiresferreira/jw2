package me.devw.wallet.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.devw.wallet.dto.AccountBalanceResponse;
import me.devw.wallet.dto.CreateAccountRequest;
import me.devw.wallet.entity.Account;
import me.devw.wallet.entity.AccountType;
import me.devw.wallet.entity.Transaction;
import me.devw.wallet.security.SecurityContextHolder;
import me.devw.wallet.service.AccountService;
import me.devw.wallet.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    AccountService accountService;

    @Inject
    TransactionService transactionService;

    @GET
    public Response getAccounts() {

        SecurityContextHolder.UserPrincipal user = SecurityContextHolder.get();

        List<AccountBalanceResponse> accounts = accountService.findBalances(UUID.fromString(user.userId()));

        return Response.ok(accounts).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccountRequest request) {

        SecurityContextHolder.UserPrincipal user = SecurityContextHolder.get();

        System.out.println(user.userId());

        Account account = new Account();
        account.setUserId(UUID.fromString(user.userId()));
        account.setName(request.name);
        account.setAccountType(request.accountType);
        account.setIcon(request.icon);
        account.setPosition((BigDecimal) request.position);
        account.setIconColor(request.icon_color);
        account.setCurrency(request.currency);
        account.setOpeningDate(request.opening_date);
        if (request.created_at != null) {
            account.setCreatedAt(request.created_at);

        } else {
            account.setCreatedAt(LocalDate.now());
        }

        accountService.save(account);


        Transaction transaction = new Transaction();

        transaction.setAmount(request.opening_balance);
        transaction.setUser_id(UUID.fromString(user.userId()));

        if (request.accountType == AccountType.LIABILITY || request.accountType == AccountType.EXPENSE) {
            transaction.setDebit_account_id(account.getId());
            transaction.setCredit_account_id(accountService.getOpeningEquity(UUID.fromString(user.userId())).getId());
        } else {
            transaction.setCredit_account_id(account.getId());
            transaction.setDebit_account_id(accountService.getOpeningEquity(UUID.fromString(user.userId())).getId());
        }
        if (request.created_at != null) {
            transaction.setCreatedAt(request.created_at);
            transaction.setTransactionDate(request.created_at);

        } else {
            transaction.setCreatedAt(LocalDate.now());
            transaction.setTransactionDate(LocalDate.now());
        }
        transaction.setAmount(request.opening_balance);
        transaction.setDescription("Opening Balance");
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