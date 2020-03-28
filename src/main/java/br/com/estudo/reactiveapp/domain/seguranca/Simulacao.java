package br.com.estudo.reactiveapp.domain.seguranca;


/*@RestController
@RequestMapping("/api/v1")
public class Simulacao {

    @GetMapping("/profiles")
    @PreAuthorize("hasRole(USER)")
    public Mono<String> getProfile(){
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> Mono.just(auth.getName()));
    }
}
*/