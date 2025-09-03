package com.project.util;

import java.util.Arrays;

import com.project.entity.Account;
import com.project.entity.Season;
import com.project.entity.Studio;
import com.project.model.SeasonId;
import com.project.repository.AccountRepository;
import com.project.repository.SeasonRepository;
import com.project.repository.StudioRepository;

public class SeedData {
    private final AccountRepository accountRepository;
    private final StudioRepository studioRepository;
    private final SeasonRepository seasonRepository;

    public SeedData(AccountRepository accountRepository, SeasonRepository seasonRepository, StudioRepository studioRepository){
        this.accountRepository = accountRepository;
        this.studioRepository = studioRepository;
        this.seasonRepository = seasonRepository;
    }

    public void seeds(){
        // seedAccounts();
        // seedStudios();
        // seedSeasons();
    }

    private void seedSeasons() {
        System.out.println("-> Seeding Seasons Table...");

        Season spring2024 = new Season();
        spring2024.setId(new SeasonId("SPRING", (short) 2024));

        Season summer2024 = new Season();
        summer2024.setId(new SeasonId("SUMMER", (short) 2024));

        Season fall2024 = new Season();
        fall2024.setId(new SeasonId("FALL", (short) 2024));

        seasonRepository.saveAll(Arrays.asList(spring2024, summer2024, fall2024));
    }

    private void seedStudios() {
        System.out.println("-> Seeding Studios Table...");
        Studio mappa = new Studio();
        mappa.setStudioName("MAPPA");

        Studio ufotable = new Studio();
        ufotable.setStudioName("ufotable");

        studioRepository.saveAll(Arrays.asList(mappa, ufotable));
    }

    private void seedAccounts() {
        System.out.println("-> Seeding Accounts Table...");
        Account admin01 = new Account();

        // admin01 - 123
        admin01.setUsername("admin01");
        admin01.setHashedPassword(HashUtil.encode("123"));
        admin01.setUserRole(Account.Role.ADMIN);


         // admin02 - 123
        Account admin02 = new Account();

        admin02.setUsername("admin02");
        admin02.setHashedPassword(HashUtil.encode("123"));
        admin02.setUserRole(Account.Role.ADMIN);

        accountRepository.saveAll(Arrays.asList(admin01, admin02));

        // user01 - 1234
        Account user01 = new Account();
        user01.setUsername("user01");
        user01.setHashedPassword(HashUtil.encode("1234"));
        user01.setUserRole(Account.Role.USER);

        // user02 - 1234
        Account user02 = new Account();
        user02.setUsername("user02");
        user02.setHashedPassword(HashUtil.encode("1234"));
        user02.setUserRole(Account.Role.USER);

        // user02 - 1234
        Account user03 = new Account();
        user03.setUsername("user03");
        user03.setHashedPassword(HashUtil.encode("1234"));
        user03.setUserRole(Account.Role.USER);

        accountRepository.saveAll(Arrays.asList(user01, user02, user03));
    }


}
