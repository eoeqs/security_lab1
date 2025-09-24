#### Проект защищенного REST API с интеграцией безопасности в CI/CD pipeline. Реализован на Java с использованием Spring Boot, включает аутентификацию через JWT токены и автоматизированную проверку кода на уязвимости.

## Эндпоинты

### Аутентификация

#### POST /auth/register - Регистрация нового пользователя.

**Тело запроса:**
```
{
"email": "test1@example.com",
"password": "password123",
"name": "Test User"
}
```

**Ответ:**
```
{
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBleGFtcGxlLmNvbSIsImlhdCI6MTc1ODY5NTI2OSwiZXhwIjoxNzU4NzgxNjY5fQ.USfa7OS7-P-t-h5DLpss8Ub4mYogxFH--tBScwdZNL4"
}
```

#### POST /auth/login - Аутентификация пользователя.

**Тело запроса:**

```
{
"email": "test1@example.com",
"password": "password123"
}
```

**Ответ:**

```
{
"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBleGFtcGxlLmNvbSIsImlhdCI6MTc1ODY5NTI3NSwiZXhwIjoxNzU4NzgxNjc1fQ.rUmdlUjktk7yB9e-JQFAYvWcepjtXm5oTcGudTOdxpo"
}
```

### Защищенные эндпоинты

Все следующие эндпоинты требуют JWT токен в заголовке:

`Authorization: Bearer <token>`

#### GET /api/data - Получение защищенных данных.

**Ответ:**

```
[
{
"content": "Sample data 1 &lt;script&gt;alert(&#39;xss&#39;)&lt;/script&gt;",
"id": "1"
}
]
```
#### POST /api/data - Создание новых данных с автоматической санитизацией.

**Тело запроса:**

```
Sample data with <script>alert('xss')</script> for testing
```

**Ответ:**

```
Data created: Sample data with &lt;script&gt;alert(&#39;xss&#39;)&lt;/script&gt; for testing
```
#### GET /api/user-info - Получение информации о пользователе.

**Ответ:**
``
```
{
"status": "authenticated",
"message": "This is protected user info"
}
```

## Меры защиты

### 1. Защита от SQL Injection (SQLi)

- **Реализация**: Использование Spring Data JPA с параметризованными запросами
- **Код**:

```
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email); 
}
```

### 2. Защита от XSS (Cross-Site Scripting)

- **Реализация**: Автоматическое экранирование HTML через `HtmlUtils.htmlEscape()`
- **Код**:

```
@GetMapping("/data")
public ResponseEntity<List<Map<String, String>>> getData() {
    List<Map<String, String>> data = List.of(
        Map.of("id", "1", "content", HtmlUtils.htmlEscape("Sample data 1 <script>alert('xss')</script>"))
    );
    return ResponseEntity.ok(data);
}
```

### 3. Безопасная аутентификация

#### JWT Токены

- **Алгоритм**: HS256 с 256-битным секретным ключом
- **Время жизни**: 24 часа
- **Валидация**: Проверка подписи и срока действия
- **Middleware**: JwtAuthenticationFilter

```
public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
}
```

#### Хеширование паролей

- **Алгоритм**: BCrypt
- **Код**:

```
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

```
user.setPassword(passwordEncoder.encode(request.password()));
```


### 4. Защита зависимостей (SCA)

- **Инструменты**: Snyk для проверки уязвимостей в зависимостях
- **Интеграция**: Автоматическая проверка при каждом push/pull request в репозиторий

### 5. Статический анализ кода (SAST)

- **Инструменты**: SpotBugs для поиска уязвимостей в коде
- **Интеграция**: В CI/CD pipeline



## Отчеты

### SAST Report - SpotBugs
![spotbugs-report.png](data/spotbugs-report.png)
https://github.com/eoeqs/security_lab1/actions/runs/17960356423/artifacts/4087913293

[INFO] BugInstance size is 0
[](https://github.com/eoeqs/security_lab1/actions/runs/17960356423/job/51082135414#step:6:1870)[INFO] Error size is 0
[](https://github.com/eoeqs/security_lab1/actions/runs/17960356423/job/51082135414#step:6:1871)[INFO] No errors/warnings found

### SCA Report - Snyk
[https://github.com/eoeqs/security_lab1/actions/runs/17960356423/artifacts/4087916077](https://github.com/eoeqs/security_lab1/actions/runs/17960356423/artifacts/4087916077)
Tested 94 dependencies for known issues, no vulnerable paths found.
![snyk-report.png](data/snyk-report.png)

### CI/CD Pipeline Status


https://github.com/eoeqs/security_lab1/actions/runs/17960356423
Все этапы pipeline успешно пройдены
