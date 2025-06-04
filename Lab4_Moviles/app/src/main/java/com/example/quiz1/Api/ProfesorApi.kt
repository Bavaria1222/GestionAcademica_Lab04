import com.example.quiz1.model.Profesor
import retrofit2.Call
import retrofit2.http.*

interface ProfesorApi {

    @GET("profesores")
    fun listar(): Call<List<Profesor>>

    @POST("profesores")
    fun insertar(@Body profesor: Profesor): Call<Void>

    @PUT("profesores")
    fun modificar(@Body profesor: Profesor): Call<Void>

    // Aquí la ruta usa {cedula} en lugar de {id}
    @DELETE("profesores/{cedula}")
    fun eliminar(@Path("cedula") cedula: String): Call<Void>
}
