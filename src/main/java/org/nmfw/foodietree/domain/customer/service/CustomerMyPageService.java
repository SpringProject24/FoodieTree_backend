package org.nmfw.foodietree.domain.customer.service;

import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nmfw.foodietree.domain.customer.dto.resp.*;
import org.nmfw.foodietree.domain.customer.entity.CustomerIssues;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.nmfw.foodietree.domain.customer.entity.value.IssueStatus;
import org.nmfw.foodietree.domain.customer.mapper.CustomerMyPageMapper;
import org.nmfw.foodietree.domain.customer.repository.CustomerMyPageRepository;
import org.nmfw.foodietree.domain.customer.repository.FavAreaRepository;
import org.nmfw.foodietree.domain.product.Util.FileUtil;
import org.nmfw.foodietree.domain.reservation.dto.resp.ReservationDetailDto;
import org.nmfw.foodietree.domain.reservation.mapper.ReservationMapper;
import org.nmfw.foodietree.domain.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.nmfw.foodietree.domain.customer.entity.value.IssueCategory.fromString;
import static org.nmfw.foodietree.domain.customer.entity.value.IssueStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerMyPageService {

    private final CustomerMyPageMapper customerMyPageMapper;
    private final ReservationMapper reservationMapper;
    private final PasswordEncoder encoder;
    private final ReservationService reservationService;

    private final FavAreaRepository favAreaRepository;

    private final CustomerMyPageRepository customerMyPageRepository;

    @Value("${env.upload.path}")
    private String uploadDir;

    /**
     * 고객 정보를 가져오는 메서드
     *
     * @param customerId 고객 ID
     * @return 고객 정보 DTO
     */
    public CustomerMyPageDto getCustomerInfo(String customerId) {
        return customerMyPageRepository.findCustomerDetails(customerId);
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월dd일 HH시mm분");

    /**
     * 고객의 예약 목록을 가져오는 메서드
     *
     * @param customerId 고객 ID
     * @return 고객 예약 목록 DTO 리스트
     */
    public List<ReservationDetailDto> getReservationList(String customerId) {
        List<ReservationDetailDto> reservations = customerMyPageRepository.findReservationsByCustomerId(customerId);

        reservations.forEach(reservation -> {
            if (reservation.getReservationTime() != null) {
                reservation.setReservationTimeF(reservation.getReservationTime().format(formatter));
            }
            if (reservation.getCancelReservationAt() != null) {
                reservation.setCancelReservationAtF(reservation.getCancelReservationAt().format(formatter));
            }
            if (reservation.getPickedUpAt() != null) {
                reservation.setPickedUpAtF(reservation.getPickedUpAt().format(formatter));
            }
            if (reservation.getPickupTime() != null) {
                reservation.setPickupTimeF(reservation.getPickupTime().format(formatter));
            }
        });
        return reservations;
    }


    /**
     * 고객 통계 정보를 가져오는 메서드
     *
     * @param customerId 고객 ID
     * @return 고객 통계 정보 DTO
     */
    public StatsDto getStats(String customerId) {
        List<ReservationDetailDto> reservations = customerMyPageRepository.findReservationsByCustomerId(customerId);

        // 예약 내역 중 pickedUpAt이 null이 아닌 것들의 리스트
        List<ReservationDetailDto> pickedUpReservations = reservations.stream()
                .filter(reservation -> reservation.getPickedUpAt() != null)
                .collect(Collectors.toList());

        // pickedUpAt이 null이 아닌 것들의 개수
        int total = pickedUpReservations.size();

        // CO2 계산
        double coTwo = total * 0.12;

        // totalPrice 계산
        int totalPrice = pickedUpReservations.stream()
                .mapToInt(ReservationDetailDto::getPrice)
                .sum();

        // money 계산
        int money = (int) (totalPrice * 0.7);

        return StatsDto.builder()
                .total(total)
                .coTwo(coTwo)
                .money(money)
                .customerId(customerId)
                .build();
    }

    public List<CustomerIssueDetailDto> getCustomerIssues(String customerId) {
        List<CustomerIssues> issues = customerMyPageMapper.findIssues(customerId);

        return issues.stream().map(issue -> CustomerIssueDetailDto.builder()
                .customerId(issue.getCustomerId())
                .nickname(issue.getNickname())
                .storeName(issue.getStoreName())
                .issueCategory(fromString(issue.getIssueCategory()))
                .issueStatus(checkIssueStatus(issue.getIssueCompleteAt()))
                .issueCompleteAt(issue.getIssueCompleteAt())
                .issueText(issue.getIssueText())
                .cancelIssueAt(issue.getCancelIssueAt())
                .build()
        ).collect(Collectors.toList());
    }

    /**
     * 이슈가 해결되어 시간이 있다면 -> Solved(해결완료)
     * 접수된 시간이 있다면 -> INPROGRESS(진행중)
     *
     * @param issueCompleteAt : 이슈가 해결된 시간
     * @return Status enum
     */
    private IssueStatus checkIssueStatus(LocalDateTime issueCompleteAt) {
        if (issueCompleteAt == null) {
            return INPROGRESS;
        } else {
            return SOLVED;
        }
    }

    public boolean updateCustomerInfo(String customerId, List<UpdateDto> updates) {
        for (UpdateDto update : updates) {
            String type = update.getType();
            String value = update.getValue();
            log.info("update type: {}, value: {}", type, value);
            if ("preferredFood".equals(type)) {
                customerMyPageMapper.addPreferenceFood(customerId, value);
                return true;
            } else if ("preferredArea".equals(type)) {
                customerMyPageMapper.addPreferenceArea(customerId, value);
                return true;
            } else if ("favStore".equals(type)) {
                customerMyPageMapper.addFavStore(customerId, value);
                return true;
            } else {
                customerMyPageMapper.updateCustomerInfo(customerId, type, value);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCustomerInfo(String customerId, List<UpdateDto> dtos) {
        for (UpdateDto delete : dtos) {
            String type = delete.getType();
            String target = delete.getValue();

            log.info("delete type: {}, target: {}", type, target);

            if ("preferredFood".equals(type)) {
                customerMyPageMapper.deletePreferenceFood(customerId, target);
                return true;
            }
            if ("preferredArea".equals(type)) {
                customerMyPageMapper.deletePreferenceArea(customerId, target);
                return true;
            }
            if ("favStore".equals(type)) {
                customerMyPageMapper.deleteFavStore(customerId, target);
                return true;
            }
        }
        return false;
    }

    public boolean updateCustomerPw(String customerId, String newPassword) {
        String encodedPw = encoder.encode(newPassword);
        customerMyPageMapper.updateCustomerInfo(customerId, "customer_password", encodedPw);
        return true;
    }

    public boolean updateProfileImg(String customerId, MultipartFile customerImg) {
        try {
            if (!customerImg.isEmpty()) {
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String imagePath = FileUtil.uploadFile(uploadDir, customerImg);
                UpdateDto dto = UpdateDto.builder()
                        .type("profile_image")
                        .value(imagePath)
                        .build();
                updateCustomerInfo(customerId, List.of(dto));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCustomerAreaInfo(String customerId, UpdateAreaDto dto) {
        String preferredArea = dto.getPreferredArea();
        String alias = dto.getAlias();

        try {
            favAreaRepository.save(FavArea.builder()
                    .customerId(customerId)
                    .preferredArea(preferredArea)
                    .alias(alias)
                    .build()
            );
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomerAreaInfo(String customerId, UpdateAreaDto dto) {

        String preferredArea = dto.getPreferredArea();
        String alias = dto.getAlias();
//            favAreaRepository.delete(FavArea.builder()
//                    .customerId(customerId)
//                    .preferredArea(preferredArea)
//                    .alias(alias)
//                    .build()
//            );
        log.info("deleteCustomerAreaInfo customerId: {}, preferredArea: {}, alias: {}", customerId, preferredArea, alias);
        try {
            favAreaRepository.deleteByCustomerIdAndPreferredAreaAndAlias(customerId, preferredArea, alias);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<UpdateAreaDto> getFavArea(String customerId) {
        return favAreaRepository.findByCustomerId(customerId).stream()
                .map(favArea -> UpdateAreaDto.builder()
                        .preferredArea(favArea.getPreferredArea())
                        .alias(favArea.getAlias())
                        .build()
                ).collect(Collectors.toList());
    }
}
