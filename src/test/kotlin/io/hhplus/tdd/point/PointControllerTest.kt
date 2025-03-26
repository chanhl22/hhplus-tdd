package io.hhplus.tdd.point

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(PointController::class)
class PointControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var pointService: PointService

    @DisplayName("유저의 포인트를 조회한다.")
    @Test
    fun createOrder() {
        //given
        val requestId = 1L

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/point/${requestId}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("유저의 포인트를 조회할 때 유저 id 타입은 숫자이다.")
    @Test
    fun createOrder2() {
        //given
        val requestId = "error"

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/point/${requestId}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
    }

    @DisplayName("유저의 포인트 충전/이용 내역을 조회한다.")
    @Test
    fun history() {
        //given
        val requestId = 1L

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/point/${requestId}/histories")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("유저의 포인트 충전/이용 내역을 조회할 때 유저 id 타입은 숫자이다.")
    @Test
    fun history2() {
        //given
        val requestId = "error"

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/point/${requestId}/histories")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
    }

    @DisplayName("유저의 포인트를 충전한다.")
    @Test
    fun charge() {
        //given
        val requestId = 1L
        val requestAmount = 1000L

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/point/${requestId}/charge")
                .content(objectMapper.writeValueAsString(requestAmount))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("유저의 포인트를 충전할 때 유저 id 타입은 숫자이다.")
    @Test
    fun charge2() {
        //given
        val requestId = "error"
        val requestAmount = 1000L

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/point/${requestId}/charge")
                .content(objectMapper.writeValueAsString(requestAmount))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
    }

    @DisplayName("유저의 포인트를 사용한다.")
    @Test
    fun use() {
        //given
        val requestId = 1L
        val requestAmount = 1000L

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/point/${requestId}/use")
                .content(objectMapper.writeValueAsString(requestAmount))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @DisplayName("유저의 포인트를 사용할 때 유저 id 타입은 숫자이다.")
    @Test
    fun use2() {
        //given
        val requestId = "error"
        val requestAmount = 1000L

        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/point/${requestId}/use")
                .content(objectMapper.writeValueAsString(requestAmount))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
    }

}