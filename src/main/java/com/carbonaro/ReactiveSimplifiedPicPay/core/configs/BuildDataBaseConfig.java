package com.carbonaro.ReactiveSimplifiedPicPay.core.configs;

import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.LegalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.entities.NaturalPerson;
import com.carbonaro.ReactiveSimplifiedPicPay.domain.mappers.IPersonMapper;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.LegalPersonRepository;
import com.carbonaro.ReactiveSimplifiedPicPay.repositories.NaturalPersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@AllArgsConstructor
public class BuildDataBaseConfig implements CommandLineRunner {

    private final LegalPersonRepository legalPersonRepository;
    private final NaturalPersonRepository naturalPersonRepository;

    private <T, R> void buildDataBase(List<T> list, ReactiveMongoRepository<T, R> repository) {

        repository
                .deleteAll()
                .thenMany(repository.saveAll(list))
                .subscribe();
    }

    @Override
    public void run(String... args) throws Exception {

        buildDataBase(new ArrayList<>(Arrays.asList(
                NaturalPerson.builder()
                        .id(null)
                        .name("João Silva")
                        .email("joao.silva@example.com")
                        .address("Rua das Flores, 123")
                        .password("senha123")
                        .cpf("123.456.789-10")
                        .birthDate(LocalDate.parse("10/05/2001", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .balance(BigDecimal.valueOf(15000.00))
                        .build()
                ,
                NaturalPerson.builder()
                        .id(null)
                        .name("Maria Oliveira")
                        .email("maria.oliveira@example.com")
                        .address("Avenida Central, 456")
                        .password("senha456")
                        .cpf("987.654.321-00")
                        .birthDate(LocalDate.parse("08/01/1997", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .balance(BigDecimal.valueOf(8000.00))
                        .build()
                ,
                NaturalPerson.builder()
                        .id(null)
                        .name("José Pereira")
                        .email("jose.pereira@example.com")
                        .address("Praça Principal, 789")
                        .password("senha789")
                        .cpf("456.789.123-00")
                        .birthDate(LocalDate.parse("22/08/1999", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .balance(BigDecimal.valueOf(130000.00))
                        .build()
                ,
                NaturalPerson.builder()
                        .id(null)
                        .name("Ana Santos")
                        .email("ana.santos@example.com")
                        .address("Alameda dos Anjos, 321")
                        .password("senha321")
                        .cpf("321.654.987-00")
                        .birthDate(LocalDate.parse("08/10/1973", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .balance(BigDecimal.valueOf(70000.00))
                        .build()
                ,
                NaturalPerson.builder()
                        .id(null)
                        .name("Carlos Souza")
                        .email("carlos.souza@example.com")
                        .address("Travessa dos Sonhos, 555")
                        .password("senha555")
                        .cpf("789.456.321-00")
                        .birthDate(LocalDate.parse("14/04/1998", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .balance(BigDecimal.valueOf(16000.00))
                        .build()
                ,
                NaturalPerson.builder()
                        .id(null)
                        .name("Lúcia Ferreira")
                        .email("lucia.ferreira@example.com")
                        .address("Rua das Estrelas, 888")
                        .password("senha888")
                        .cpf("159.357.852-00")
                        .birthDate(LocalDate.parse("05/05/2005", DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                        .balance(BigDecimal.valueOf(140000.00))
                        .build()
        )), naturalPersonRepository);

        // <===============================================================================================================> //

        List<LegalPerson> legalPersonList = new ArrayList<>(Arrays.asList(
                LegalPerson.builder()
                        .id(null)
                        .name("ABC Incorporadora")
                        .email("contato@abcincorp.com")
                        .address("Rua das Construções, 10")
                        .password("incorppass123")
                        .cnpj("12.345.678/0001-90")
                        .balance(BigDecimal.valueOf(120000.00))
                        .monthlyBilling(BigDecimal.valueOf(20000.00))
                        .annualBilling(BigDecimal.valueOf(240000.00))
                        .companySize(null)
                        .partners(new ArrayList<>(Arrays.asList(
                                IPersonMapper.INSTANCE.toNaturalPersonResponse(naturalPersonRepository.findByCpf("159.357.852-00").block()),
                                IPersonMapper.INSTANCE.toNaturalPersonResponse(naturalPersonRepository.findByCpf("789.456.321-00").block())
                        )))
                        .build()
                ,
                LegalPerson.builder()
                        .id(null)
                        .name("XYZ Consultoria")
                        .email("contato@xyzconsult.com")
                        .address("Avenida das Ideias, 20")
                        .password("consultpass456")
                        .cnpj("98.765.432/0001-10")
                        .balance(BigDecimal.valueOf(2000000.00))
                        .monthlyBilling(BigDecimal.valueOf(155000.00))
                        .annualBilling(BigDecimal.valueOf(1860000.00))
                        .companySize(null)
                        .partners(new ArrayList<>(Arrays.asList(
                                IPersonMapper.INSTANCE.toNaturalPersonResponse(naturalPersonRepository.findByCpf("987.654.321-00").block()),
                                IPersonMapper.INSTANCE.toNaturalPersonResponse(naturalPersonRepository.findByCpf("321.654.987-00").block())
                        )))
                        .build()
                ,
                LegalPerson.builder()
                        .id(null)
                        .name("DEF Tecnologia")
                        .email("contato@deftech.com")
                        .address("Praça da Inovação, 30")
                        .password("techpass789")
                        .cnpj("45.678.901/0001-20")
                        .balance(BigDecimal.valueOf(120000000.00))
                        .monthlyBilling(BigDecimal.valueOf(45000000.00))
                        .annualBilling(BigDecimal.valueOf(540000000.00))
                        .companySize(null)
                        .partners(new ArrayList<>(Arrays.asList(
                                IPersonMapper.INSTANCE.toNaturalPersonResponse(naturalPersonRepository.findByCpf("123.456.789-10").block()),
                                IPersonMapper.INSTANCE.toNaturalPersonResponse(naturalPersonRepository.findByCpf("456.789.123-00").block())
                        )))
                        .build()
                        ));

        legalPersonList.forEach(LegalPerson::setCompanySize);
        buildDataBase(legalPersonList, legalPersonRepository);
    }
}