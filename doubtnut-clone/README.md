## Doubtnut-like Q&A (Minimal)

### Backend (Spring Boot + MySQL)
- Java 17, Spring Boot 3, JPA, Web
- MySQL: database `doubtnut_clone`, user `root`/`root`

Endpoints:
- `POST /api/auth/google` { name, email, pictureUrl }
- `GET /api/questions`
- `POST /api/questions` { title, description, userId }
- `POST /api/answers` { questionId, content, userId }

Run:
1. Ensure MySQL is running and create database `doubtnut_clone`.
2. Update creds in `backend/src/main/resources/application.properties` if needed.
3. Build and run with Maven (install JDK 17 + Maven):
   - `mvn -q -DskipTests spring-boot:run`

### Frontend (React + Tailwind)
- Vite React (TypeScript), Tailwind
- Google OAuth via `@react-oauth/google` (frontend-only)

Setup:
1. Create `frontend/.env`:
   - `VITE_GOOGLE_CLIENT_ID=YOUR_GOOGLE_OAUTH_CLIENT_ID`
2. Install deps: `npm install` inside `frontend`.
3. Run: `npm run dev` (serves on `http://localhost:5173`).

Flow:
1. Login page → Google button → stores `{name,email,pictureUrl}` to `localStorage` and calls backend `/api/auth/google` to upsert user.
2. Home shows all questions and their answers from backend.
3. Ask page posts a question.
4. Add Answer inline under each question.


