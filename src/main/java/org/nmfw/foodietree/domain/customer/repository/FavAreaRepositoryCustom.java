package org.nmfw.foodietree.domain.customer.repository;

import org.nmfw.foodietree.domain.customer.dto.resp.UpdateAreaDto;
import org.nmfw.foodietree.domain.customer.entity.FavArea;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavAreaRepositoryCustom {

    List<UpdateAreaDto> findFavAreaByCustomerId(String customerId);
}
