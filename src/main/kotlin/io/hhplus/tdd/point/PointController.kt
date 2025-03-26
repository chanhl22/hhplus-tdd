package io.hhplus.tdd.point

import io.hhplus.tdd.point.history.response.PointHistoryResponse
import io.hhplus.tdd.point.user.request.UserPointRequest
import io.hhplus.tdd.point.user.response.UserPointResponse
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
class PointController (private val pointService: PointService){
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("/{id}")
    fun point(
        @PathVariable id: Long,
    ): UserPointResponse {
        return pointService.findPoint(id)
    }

    /**
     * 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("/{id}/histories")
    fun history(
        @PathVariable id: Long,
    ): List<PointHistoryResponse> {
        return pointService.findPointHistory(id)
    }

    /**
     * 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("/{id}/charge")
    fun charge(
        @PathVariable id: Long,
        @RequestBody @Valid request: UserPointRequest,
    ): UserPointResponse {
        return pointService.chargePoint(id, request.amount)
    }

    /**
     * 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("/{id}/use")
    fun use(
        @PathVariable id: Long,
        @RequestBody @Valid request: UserPointRequest,
    ): UserPointResponse {
        return pointService.usePoint(id, request.amount)
    }
}