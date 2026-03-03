package com.nelumbo.open_flow_coworking.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreatePageable {
    public static Pageable buildPageable(int page, int size, String... sortParams) {
        Sort sort = buildSort(sortParams);

        if (size == -1) {
            return sort.isSorted()
                    ? Pageable.unpaged(sort)
                    : Pageable.unpaged();
        }

        page = Math.max(page - 1, 0);

        return sort.isSorted()
                ? PageRequest.of(page, size, sort)
                : PageRequest.of(page, size);
    }

    private static Sort buildSort(String[] sortParams) {
        if (sortParams == null || sortParams.length == 0) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = Arrays.stream(sortParams)
                .map(param -> param.split(","))
                .filter(parts -> parts.length >= 1 && !parts[0].isBlank())
                .map(parts -> {
                    String property = parts[0].trim();
                    Sort.Direction direction = parts.length > 1
                            ? Sort.Direction.fromOptionalString(parts[1].trim()).orElse(Sort.Direction.ASC)
                            : Sort.Direction.ASC;
                    return new Sort.Order(direction, property);
                })
                .collect(Collectors.toList());

        return orders.isEmpty() ? Sort.unsorted().descending() : Sort.by(orders).descending();
    }
}
