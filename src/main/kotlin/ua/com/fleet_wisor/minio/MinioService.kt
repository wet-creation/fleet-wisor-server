package ua.com.fleet_wisor.minio

import io.ktor.http.*
import io.minio.*
import io.minio.errors.MinioException
import ua.com.fleet_wisor.utils.getConfig
import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.util.*


object MinioService {
    private const val BUCKET_NAME = "images"
    private val client: MinioClient = MinioClient.Builder()
        .endpoint(getConfig("MINIO_URL"))
        .credentials(getConfig("MINIO_ROOT_USER"), getConfig("MINIO_ROOT_PASSWORD"))
        .build()

    fun init() {
        try {
            val bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build())

            if (!bucketExists) {
                client.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(BUCKET_NAME)
                        .build()
                )
            }
        } catch (e: MinioException) {
            println("Client minio exception: ${e.message}")
        }

    }

    fun uploadPhotoToMinio(inputStream: InputStream, contentType: String): String {
        val imgName = generateName(fileType = contentType)
        try {
            client.putObject(
                PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .`object`(imgName)
                    .stream(inputStream, inputStream.available().toLong(), -1)
                    .contentType(contentType)
                    .build()
            )

        } catch (e: MinioException) {
            println("Client minio exception: ${e.message}")
        }
        return imgName
    }

    fun generateName(fileType: String): String {
        return UUID.randomUUID().toString() + getExtension(
            fileType
        )
    }

    fun load(name: String): InputStream {
        try {
            val response = client.getObject(
                GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .`object`(name)
                    .build()
            )
            return response
        } catch (e: IOException) {
            throw IllegalArgumentException("Failed to load image: $name", e)
        } catch (e: GeneralSecurityException) {
            throw IllegalArgumentException("Failed to load image: $name", e)
        } catch (e: MinioException) {
            throw IllegalArgumentException("Failed to load image: $name", e)
        }
    }

    fun remove(name: String?) {
        try {
            client.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .`object`(name)
                    .build()
            )
        } catch (e: GeneralSecurityException) {
            throw IllegalArgumentException(
                "Unable to delete object $name from bucket $BUCKET_NAME"
            )
        } catch (e: IOException) {
            throw IllegalArgumentException(
                "Unable to delete object $name from bucket $BUCKET_NAME"

            )
        } catch (e: MinioException) {
            throw IllegalArgumentException(
                "Unable to delete object $name from bucket $BUCKET_NAME"
            )
        }
    }

    private fun getExtension(contentType: String): String {
        return when (contentType) {
            "image/png" -> ".png"
            "image/svg+xml" -> ".svg"
            else -> ".jpeg"
        }
    }
    fun getContentType(extension: String): ContentType {
        return when (extension) {
            ".png" -> ContentType.Image.PNG
            ".svg" -> ContentType.Image.SVG
            else -> ContentType.Image.JPEG
        }
    }


}