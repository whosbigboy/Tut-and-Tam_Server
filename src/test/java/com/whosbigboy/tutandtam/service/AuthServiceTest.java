package com.whosbigboy.tutandtam.service;

import com.whosbigboy.tutandtam.dto.AuthDtos;
import com.whosbigboy.tutandtam.entity.Contract;
import com.whosbigboy.tutandtam.entity.MemberList;
import com.whosbigboy.tutandtam.entity.Tourist;
import com.whosbigboy.tutandtam.repository.ContractRepository;
import com.whosbigboy.tutandtam.repository.GuideRepository;
import com.whosbigboy.tutandtam.repository.ManagerRepository;
import com.whosbigboy.tutandtam.repository.MemberListRepository;
import com.whosbigboy.tutandtam.repository.TourRepository;
import com.whosbigboy.tutandtam.repository.TouristRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private ContractRepository contractRepository;
    @Mock
    private MemberListRepository memberListRepository;
    @Mock
    private TouristRepository touristRepository;
    @Mock
    private GuideRepository guideRepository;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TourRepository tourRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void checkContract_returnsAtMostEightMembers() {
        Contract contract = new Contract();
        contract.setId("c-1");
        contract.setNumHash("CN-100");

        List<MemberList> members = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> {
                    MemberList member = new MemberList();
                    member.setId("m-" + i);
                    member.setContractId("c-1");
                    member.setTouristId("t-" + i);
                    return member;
                }).toList();

        List<Tourist> tourists = IntStream.rangeClosed(1, 8)
                .mapToObj(i -> {
                    Tourist tourist = new Tourist();
                    tourist.setId("t-" + i);
                    tourist.setFio("Tourist " + i);
                    return tourist;
                }).toList();

        when(contractRepository.findByNumHash("CN-100")).thenReturn(Optional.of(contract));
        when(memberListRepository.findByContractId("c-1")).thenReturn(members);
        when(touristRepository.findByIdIn(anyCollection())).thenReturn(tourists);

        AuthDtos.ContractAuthResponse response = authService.checkContract("CN-100");

        assertThat(response.found()).isTrue();
        assertThat(response.members()).hasSize(8);
    }
}
