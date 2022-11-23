package net.ambitious.agentnotice.request

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.server.ResponseStatusException
import javax.servlet.http.HttpServletRequest

class AuthResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(AuthToken::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): String {
        val request = webRequest.nativeRequest as HttpServletRequest
        val authorization = request.getHeader("Authorization")
        if (authorization.isNullOrEmpty()) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }
        if (!authorization.startsWith("Bearer ") || authorization.length <= 7) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }
        return authorization.substring(7)
    }
}