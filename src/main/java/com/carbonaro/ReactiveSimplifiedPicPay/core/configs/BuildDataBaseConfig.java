package com.carbonaro.ReactiveSimplifiedPicPay.core.configs;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.Transaction;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.LegalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.NaturalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BuildDataBaseConfig implements CommandLineRunner {

    private final TransactionRepository transactionRepository;
    private final LegalPersonRepository legalPersonRepository;
    private final NaturalPersonRepository naturalPersonRepository;

    @Override
    public void run(String... args) {

        Mono.just(naturalPersonRepository.deleteAll().subscribe())
                .doOnSubscribe(subscription -> naturalPersonRepository.saveAll(getNaturalPersons()).subscribe())
                .doOnSubscribe(subscription -> legalPersonRepository.deleteAll().subscribe())
                .doOnSubscribe(subscription -> legalPersonRepository.saveAll(getLegalPersons()).subscribe())
                .doOnSubscribe(subscription -> transactionRepository.deleteAll().subscribe())
                .doOnSubscribe(subscription -> transactionRepository.saveAll(getTransactions()).subscribe())
                .doOnSubscribe(subscription -> log.warn("Database has been reset and populated with default data successfully"))
                .subscribe();
    }

    private List<NaturalPerson> getNaturalPersons() {

        final String DEFAULT_DATE = "dd/MM/yyyy";

        return new ArrayList<>(Arrays.asList(
                NaturalPerson.builder()
                        .id(null)
                        .name("João Silva")
                        .email("joao.silva@example.com")
                        .address("Rua das Flores, 123")
                        .password("senha123")
                        .cpf("12345678910")
                        .birthDate(LocalDate.parse("10/05/2001", DateTimeFormatter.ofPattern(DEFAULT_DATE)))
                        .balance(BigDecimal.valueOf(15000.00))
                        .build(),
                NaturalPerson.builder()
                        .id(null)
                        .name("Maria Oliveira")
                        .email("maria.oliveira@example.com")
                        .address("Avenida Central, 456")
                        .password("senha456")
                        .cpf("98765432100")
                        .birthDate(LocalDate.parse("08/01/1997", DateTimeFormatter.ofPattern(DEFAULT_DATE)))
                        .balance(BigDecimal.valueOf(8000.00))
                        .build(),
                NaturalPerson.builder()
                        .id(null)
                        .name("José Pereira")
                        .email("jose.pereira@example.com")
                        .address("Praça Principal, 789")
                        .password("senha789")
                        .cpf("45678912300")
                        .birthDate(LocalDate.parse("22/08/1999", DateTimeFormatter.ofPattern(DEFAULT_DATE)))
                        .balance(BigDecimal.valueOf(130000.00))
                        .build(),
                NaturalPerson.builder()
                        .id(null)
                        .name("Ana Santos")
                        .email("ana.santos@example.com")
                        .address("Alameda dos Anjos, 321")
                        .password("senha321")
                        .cpf("32165498700")
                        .birthDate(LocalDate.parse("08/10/1973", DateTimeFormatter.ofPattern(DEFAULT_DATE)))
                        .balance(BigDecimal.valueOf(70000.00))
                        .build(),
                NaturalPerson.builder()
                        .id(null)
                        .name("Carlos Souza")
                        .email("carlos.souza@example.com")
                        .address("Travessa dos Sonhos, 555")
                        .password("senha555")
                        .cpf("78945632100")
                        .birthDate(LocalDate.parse("14/04/1998", DateTimeFormatter.ofPattern(DEFAULT_DATE)))
                        .balance(BigDecimal.valueOf(16000.00))
                        .build(),
                NaturalPerson.builder()
                        .id(null)
                        .name("Lúcia Ferreira")
                        .email("lucia.ferreira@example.com")
                        .address("Rua das Estrelas, 888")
                        .password("senha888")
                        .cpf("15935785200")
                        .birthDate(LocalDate.parse("05/05/2005", DateTimeFormatter.ofPattern(DEFAULT_DATE)))
                        .balance(BigDecimal.valueOf(140000.00))
                        .build()));
    }

    private List<LegalPerson> getLegalPersons() {

        List<LegalPerson> list = new ArrayList<>(Arrays.asList(
                LegalPerson.builder()
                        .id(null)
                        .name("ABC Incorporadora")
                        .email("contato@abcincorp.com")
                        .address("Rua das Construções, 10")
                        .password("incorppass123")
                        .cnpj("12345678000190")
                        .employeesNumber(48)
                        .balance(BigDecimal.valueOf(120000.00))
                        .monthlyBilling(BigDecimal.valueOf(20000.00))
                        .annualBilling(BigDecimal.valueOf(240000.00))
                        .companySize(null)
                        .partners(Mono
                                .zip(naturalPersonRepository.findByCpf("15935785200"),
                                        naturalPersonRepository.findByCpf("78945632100"))
                                .map(tuple -> Arrays.asList(tuple.getT1(), tuple.getT2())).block())
                        .build(),
                LegalPerson.builder()
                        .id(null)
                        .name("XYZ Consultoria")
                        .email("contato@xyzconsult.com")
                        .address("Avenida das Ideias, 20")
                        .password("consultpass456")
                        .cnpj("98765432000110")
                        .employeesNumber(580)
                        .balance(BigDecimal.valueOf(2000000.00))
                        .monthlyBilling(BigDecimal.valueOf(155000.00))
                        .annualBilling(BigDecimal.valueOf(1860000.00))
                        .companySize(null)
                        .partners(Mono
                                .zip(naturalPersonRepository.findByCpf("98765432100"),
                                        naturalPersonRepository.findByCpf("32165498700"))
                                .map(tuple -> Arrays.asList(tuple.getT1(), tuple.getT2())).block())
                        .build(),
                LegalPerson.builder()
                        .id(null)
                        .name("DEF Tecnologia")
                        .email("contato@deftech.com")
                        .address("Praça da Inovação, 30")
                        .password("techpass789")
                        .cnpj("45678901000120")
                        .employeesNumber(1150)
                        .balance(BigDecimal.valueOf(120000000.00))
                        .monthlyBilling(BigDecimal.valueOf(45000000.00))
                        .annualBilling(BigDecimal.valueOf(540000000.00))
                        .companySize(null)
                        .partners(Mono
                                .zip(naturalPersonRepository.findByCpf("12345678910"),
                                        naturalPersonRepository.findByCpf("45678912300"))
                                .map(tuple -> Arrays.asList(tuple.getT1(), tuple.getT2())).block())
                        .build()
        ));

        list.forEach(LegalPerson::setCompanySize);
        return list;
    }

    private List<Transaction> getTransactions() {

        List<NaturalPerson> naturalPersons = getNaturalPersons();
        List<LegalPerson> legalPersons = getLegalPersons();
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 40; i++) {

            String sender = naturalPersons.get(random.nextInt(naturalPersons.size())).getCpf();
            boolean receiverIsNatural = random.nextBoolean();
            String receiver;

            do {
                receiver = receiverIsNatural
                        ? naturalPersons.get(random.nextInt(naturalPersons.size())).getCpf()
                        : legalPersons.get(random.nextInt(legalPersons.size())).getCnpj();
            } while (receiver.equals(sender));

            BigDecimal transactionValue = BigDecimal.valueOf(random.nextDouble() * 30000)
                    .setScale(2, RoundingMode.HALF_UP);
            LocalDateTime transactionDate = LocalDateTime.now().minusDays(random.nextInt(730));

            transactions.add(Transaction.builder()
                    .id(null)
                    .senderDocument(sender)
                    .receiverDocument(receiver)
                    .transactionValue(transactionValue)
                    .transactionDate(transactionDate)
                    .build());
        }

        return transactions;
    }

}
