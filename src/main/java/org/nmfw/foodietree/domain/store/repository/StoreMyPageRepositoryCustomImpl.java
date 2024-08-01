import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.nmfw.foodietree.domain.product.dto.response.ProductInfoDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreCheckDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreHolidayDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageCalendarModalDto;
import org.nmfw.foodietree.domain.store.dto.resp.StoreMyPageDto;
import org.nmfw.foodietree.domain.store.repository.StoreMyPageRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
public class StoreMyPageRepositoryCustomImpl implements StoreMyPageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public StoreMyPageRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public StoreMyPageDto getStoreMyPageInfo(String storeId) {
        QStore qStore = QStore.store;
        return queryFactory.select(new QStoreMyPageDto(qStore.storeId, qStore.storeName, qStore.address,
                        qStore.storeImg, qStore.category, qStore.approve, qStore.price, qStore.productCnt,
                        qStore.openAt, qStore.closedAt, qStore.storeLicenseNumber))
                .from(qStore)
                .where(qStore.storeId.eq(storeId))
                .fetchOne();
    }

    @Override
    public List<StoreMyPageCalendarModalDto> getStoreMyPageCalendarModalInfo(String storeId, LocalDate date) {
        QStore qStore = QStore.store;
        QProduct qProduct = QProduct.product;

        return queryFactory.select(new QStoreMyPageCalendarModalDto(qProduct.pickupTime, qStore.openAt,
                        qStore.closedAt, qStore.productCnt, qProduct.canceledByStoreAt))
                .from(qStore)
                .leftJoin(qProduct).on(qProduct.store.storeId.eq(qStore.storeId).and(qProduct.pickupTime.dateEq(date)))
                .where(qStore.storeId.eq(storeId))
                .fetch();
    }

    @Override
    @Transactional
    public void updateProductAuto(String storeId, LocalDate pickupTime) {
        // Implement updateProductAuto using QueryDSL if needed
    }

    @Override
    @Transactional
    public void cancelProductByStore(String storeId, LocalDate pickupTime) {
        // Implement cancelProductByStore using QueryDSL if needed
    }

    @Override
    public List<StoreCheckDto> getAllStore() {
        QStore qStore = QStore.store;
        return queryFactory.select(new QStoreCheckDto(qStore.storeId, qStore.storeName, qStore.productCnt,
                        qStore.openAt, qStore.closedAt))
                .from(qStore)
                .fetch();
    }

    @Override
    @Transactional
    public void setHoliday(String storeId, LocalDate holidays) {
        // Implement setHoliday using QueryDSL if needed
    }

    @Override
    @Transactional
    public void undoHoliday(String storeId, LocalDate holidays) {
        // Implement undoHoliday using QueryDSL if needed
    }

    @Override
    public List<StoreHolidayDto> getHolidays(String storeId) {
        QStoreHoliday qStoreHoliday = QStoreHoliday.storeHoliday;
        return queryFactory.select(new QStoreHolidayDto(qStoreHoliday.storeId, qStoreHoliday.holidays))
                .from(qStoreHoliday)
                .where(qStoreHoliday.storeId.eq(storeId))
                .fetch();
    }

    @Override
    public List<ProductInfoDto> getProductCntByDate(String storeId, LocalDate date) {
        QProduct qProduct = QProduct.product;
        QReservation qReservation = QReservation.reservation;

        return queryFactory.select(new QProductInfoDto(qProduct.pickupTime, qProduct.productUploadDate,
                        qProduct.canceledByStoreAt, qReservation.reservationTime, qReservation.cancelReservationAt,
                        qReservation.pickedUpAt))
                .from(qProduct)
                .leftJoin(qReservation).on(qProduct.productId.eq(qReservation.productId))
                .where(qProduct.store.storeId.eq(storeId).and(qProduct.productUploadDate.dateEq(date)))
                .fetch();
    }

    @Override
    public long countPickedUpProductsByDate(String storeId, LocalDate date) {
        QProduct qProduct = QProduct.product;
        QReservation qReservation = QReservation.reservation;

        BooleanExpression condition = qProduct.store.storeId.eq(storeId)
                .and(qReservation.pickedUpAt.dateEq(date));

        return queryFactory.select(qProduct.count())
                .from(qProduct)
                .leftJoin(qReservation).on(qProduct.productId.eq(qReservation.productId))
                .where(condition)
                .fetchCount();
    }
}
