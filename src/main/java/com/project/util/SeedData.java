package com.project.util;

import java.util.Arrays;
import com.project.util.HashUtil;
import com.project.entity.Account;
import com.project.entity.Season;
import com.project.entity.Studio;
import com.project.model.SeasonId;
import com.project.repository.AccountRepository;
import com.project.repository.SeasonRepository;
import com.project.repository.StudioRepository;

import jakarta.persistence.EntityManagerFactory;

public class SeedData {
    public static void seeds() {
        EntityManagerFactory emf = JpaUtil.getEntityManagerFactory();
        AccountRepository accountRepository = new AccountRepository(emf);
        SeasonRepository seasonRepository = new SeasonRepository(emf);
        StudioRepository studioRepository = new StudioRepository(emf);

        if(accountRepository.count() > 0 || studioRepository.count() > 0 || seasonRepository.count() > 0) {
            System.out.println("-> Seeding skipped. Data already exists.");
            return;
        }

        System.out.println("-> Seeding started...");
        seedAccounts(accountRepository);
        seedStudios(studioRepository);
        seedSeasons(seasonRepository);
        System.out.println("-> Seeding completed.");
    }



    private static void seedSeasons(SeasonRepository seasonRepository) {
        System.out.println("-> Seeding Seasons Table...");

        Season winter2023 = new Season();
        winter2023.setId(new SeasonId("WINTER", (short) 2023));

        Season spring2023 = new Season();
        spring2023.setId(new SeasonId("SPRING", (short) 2023));

        Season summer2023 = new Season();
        summer2023.setId(new SeasonId("SUMMER", (short) 2023));

        Season fall2023 = new Season();
        fall2023.setId(new SeasonId("FALL", (short) 2023));

        Season winter2024 = new Season();
        winter2024.setId(new SeasonId("WINTER", (short) 2024));

        Season spring2024 = new Season();
        spring2024.setId(new SeasonId("SPRING", (short) 2024));

        Season summer2024 = new Season();
        summer2024.setId(new SeasonId("SUMMER", (short) 2024));

        Season fall2024 = new Season();
        fall2024.setId(new SeasonId("FALL", (short) 2024));

        seasonRepository.saveAll(Arrays.asList(
        winter2023, spring2023, summer2023, fall2023,
            winter2024, spring2024,summer2024, fall2024));
    }

    private static void seedStudios(StudioRepository studioRepository) {
        System.out.println("-> Seeding Studios Table...");
        Studio mappa = new Studio();
        mappa.setStudioName("MAPPA");
        mappa.setBestAnimes("Jujutsu K,imetsu no Yaiba, Chains");

        Studio ufotable = new Studio();
        ufotable.setStudioName("ufotable");
        ufotable.setBestAnimes("Fate Series, Demon Slayer, God Eater");

        studioRepository.saveAll(Arrays.asList(mappa, ufotable));
    }

    private static void seedAccounts(AccountRepository accountRepository) {
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
