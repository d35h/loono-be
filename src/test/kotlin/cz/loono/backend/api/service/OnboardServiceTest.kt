package cz.loono.backend.api.service

import cz.loono.backend.api.dto.TypeDTO
import cz.loono.backend.api.dto.UserDTO
import cz.loono.backend.data.model.User
import cz.loono.backend.data.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.Date

class OnboardServiceTest {

    @InjectMocks
    private lateinit var onboardService: OnboardService

    @Mock
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun initMocks() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun transformationTest() {
        val date = Date()
        val userDto = UserDTO(
            birthDate = date,
            sex = 'M',
            email = "test@test.com",
            notificationEmail = "notify@test.com",
            salutation = "Shrek",
            type = TypeDTO.LOONO
        )

        val captor: ArgumentCaptor<User> = ArgumentCaptor.forClass(User::class.java)

        onboardService.onboard(userDto)
        verify(userRepository, times(1)).save(captor.capture())

        val user = captor.value
        assert(
            user.equals(
                User(
                    birthDate = date,
                    sex = userDto.sex,
                    email = userDto.email,
                    notificationEmail = userDto.notificationEmail,
                    salutation = userDto.salutation,
                    type = userDto.type.id
                )
            )
        )
    }
}
