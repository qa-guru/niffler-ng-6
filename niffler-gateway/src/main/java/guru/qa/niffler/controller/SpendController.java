package guru.qa.niffler.controller;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.UserDataClient;
import guru.qa.niffler.service.api.RestSpendClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/spends")
public class SpendController {

    private final RestSpendClient restSpendClient;
    private final UserDataClient userDataClient;

    @Autowired
    public SpendController(RestSpendClient restSpendClient, UserDataClient userDataClient) {
        this.restSpendClient = restSpendClient;
        this.userDataClient = userDataClient;
    }

    @GetMapping("/{id}")
    public SpendJson getSpend(@PathVariable("id") String id,
                              @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restSpendClient.getSpend(id, username);
    }

    @GetMapping("/all")
    public List<SpendJson> getSpends(@AuthenticationPrincipal Jwt principal,
                                     @RequestParam(required = false) DataFilterValues filterPeriod,
                                     @RequestParam(required = false) CurrencyValues filterCurrency) {
        String username = principal.getClaim("sub");
        return restSpendClient.getSpends(username, filterPeriod, filterCurrency);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public SpendJson addSpend(@Valid @RequestBody SpendJson spend,
                              @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return restSpendClient.addSpend(spend.addUsername(username));
    }

    @PatchMapping("/edit")
    public SpendJson editSpend(@Valid @RequestBody SpendJson spend,
                               @AuthenticationPrincipal Jwt principal) {
        if (spend.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id should be present");
        }
        String username = principal.getClaim("sub");
        return restSpendClient.editSpend(spend.addUsername(username));
    }

    @DeleteMapping("/remove")
    public void deleteSpends(@AuthenticationPrincipal Jwt principal,
                             @RequestParam List<String> ids) {
        String username = principal.getClaim("sub");
        restSpendClient.deleteSpends(username, ids);
    }
}
