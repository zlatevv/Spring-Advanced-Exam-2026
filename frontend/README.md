# RareManuscripts — Frontend (React SPA)

Frontend for the "RareManuscripts Digital Preservation Portal" exam project (SoftUni Spring Advanced, June 2026). Built
with React 18 + React Router, talking to your Spring Boot **Main application** REST API (this frontend counts toward the
"Use Angular/React/Vue for the frontend" bonus, +5pts).

## Domain

Researchers browse a manuscript catalog, request study access to restricted manuscripts, and — once approved — reserve a
reading-room session. Curators maintain the catalog and review requests. Curators can also send a manuscript to the
separate **Digitization & Conservation microservice** for scanning, via a Feign Client call from the Main app.

## Roles

- **RESEARCHER** — browse catalog, request access, add study notes, reserve/cancel own reading-room slots, view/edit own
  profile.
- **CURATOR** — everything above, plus manage the catalog (create/edit manuscripts, toggle visibility), approve/reject
  access requests, manage reservations, trigger digitization jobs.
- **ADMIN** — everything above, plus manage user roles.

## Running

```bash
npm install
npm run dev        # http://localhost:5173, proxies /api to http://localhost:8080
```

Point `vite.config.js`'s proxy target at wherever your Main application runs.

## Pages (10 required, ≤1 static)

| #  | Route                  | Page                                              | Type       |
|----|------------------------|---------------------------------------------------|------------|
| 1  | `/login`               | Sign in                                           | dynamic    |
| 2  | `/register`            | Request an account                                | dynamic    |
| 3  | `/`                    | Home (featured manuscripts)                       | dynamic    |
| 4  | `/catalog`             | Browse & search catalog                           | dynamic    |
| 5  | `/catalog/:id`         | Manuscript detail, request access, study notes    | dynamic    |
| 6  | `/my-requests`         | Researcher's access requests + reserve slot       | dynamic    |
| 7  | `/my-reservations`     | Researcher's reservations, cancel                 | dynamic    |
| 8  | `/manage/requests`     | Curator/Admin: approve/reject requests            | dynamic    |
| 9  | `/manage/manuscripts`  | Curator/Admin: CRUD catalog, visibility, digitize | dynamic    |
| 10 | `/manage/reservations` | Curator/Admin: all reservations                   | dynamic    |
| 11 | `/manage/digitization` | Curator/Admin: microservice job dashboard         | dynamic    |
| 12 | `/profile`             | View/edit own profile                             | dynamic    |
| 13 | `/admin/users`         | Admin: manage roles                               | dynamic    |
| 14 | `/about`               | Static info                                       | **static** |

## Valid domain functionalities implemented (main app needs ≥6, excluding User-only actions)

1. Submit an access request (`ManuscriptDetailPage`)
2. Approve / reject an access request (`RequestManagementPage`)
3. Reserve a reading-room slot (`MyRequestsPage`)
4. Cancel a reservation (`MyReservationsPage`, `ReservationsOverviewPage`)
5. Create a manuscript (`ManuscriptManagementPage`)
6. Edit a manuscript / toggle visibility (`ManuscriptManagementPage`)
7. Request digitization — triggers Feign call to microservice (`ManuscriptManagementPage`)
8. Add a study note (`ManuscriptDetailPage`)
9. Delete a study note (`ManuscriptDetailPage`)

## Entities you need to build on the backend

**Main application** (needs ≥3 domain entities + User as technical entity):

- `Manuscript` — id (UUID), title, author, era, originRegion, description, conservationStatus, visibility,
  digitizationStatus, createdAt
- `AccessRequest` — id (UUID), manuscript ↔ manuscript, researcher ↔ user, purpose, status, requestedDate, decidedAt,
  decidedBy
- `ReadingRoomReservation` — id (UUID), accessRequest ↔ AccessRequest, slotDate, slotTime, status
- `StudyNote` — id (UUID), manuscript ↔ Manuscript, author ↔ User, content, createdAt
- `User` (technical, not counted) — id (UUID), fullName, email, passwordHash, role, institution

**REST microservice** ("Digitization & Conservation Service", needs ≥1 entity, ≥2 functionalities):

- `DigitizationJob` — id (UUID), manuscriptId, priority, status, technician, requestedAt, completedAt

Feign calls from Main app → microservice:

- `POST /jobs` — create job (used when a curator requests digitization) — **write**
- `PUT /jobs/{id}/status` — update job status/assign technician — **write**
- `GET /jobs/manuscript/{manuscriptId}` — read current job status — **read**

## Full REST API contract expected by this frontend

### Auth

- `POST /api/auth/register` — body `{fullName, email, password}` → 201
- `POST /api/auth/login` — body `{email, password}` → `{token, user:{id, fullName, email, role}}`
- `GET /api/auth/me` — auth required → current user

### Manuscripts

- `GET /api/manuscripts?search=&era=&page=&size=` — public → `{content, totalPages, totalElements, page}`
- `GET /api/manuscripts/{id}` — public if PUBLIC, else authenticated
- `POST /api/manuscripts` — CURATOR/ADMIN
- `PUT /api/manuscripts/{id}` — CURATOR/ADMIN
- `PUT /api/manuscripts/{id}/visibility` — CURATOR/ADMIN — body `{visibility}`
- `POST /api/manuscripts/{id}/digitize` — CURATOR/ADMIN — body `{priority}` (Feign write call)
- `GET /api/manuscripts/{id}/digitization-status` — CURATOR/ADMIN (Feign read call)

### Access Requests

- `POST /api/access-requests` — RESEARCHER — body `{manuscriptId, purpose}`
- `GET /api/access-requests/mine` — RESEARCHER
- `GET /api/access-requests?status=` — CURATOR/ADMIN
- `PUT /api/access-requests/{id}/decision` — CURATOR/ADMIN — body `{decision}`

### Reservations

- `POST /api/reservations` — RESEARCHER — body `{accessRequestId, slotDate, slotTime}`
- `GET /api/reservations/mine` — RESEARCHER
- `GET /api/reservations` — CURATOR/ADMIN
- `DELETE /api/reservations/{id}` — owner or CURATOR/ADMIN

### Study Notes

- `GET /api/manuscripts/{id}/notes` — authenticated
- `POST /api/manuscripts/{id}/notes` — authenticated — body `{content}`
- `DELETE /api/notes/{id}` — author or ADMIN

### Users

- `GET /api/users/me/profile` — authenticated
- `PUT /api/users/me/profile` — authenticated — body `{fullName, email, institution}`
- `GET /api/users` — ADMIN
- `PUT /api/users/{id}/role` — ADMIN — body `{role}`

## Security expectations

- Open (public): `GET /api/manuscripts`, `GET /api/manuscripts/{id}` (when PUBLIC)
- Authenticated (any logged-in user): notes, own profile, own requests/reservations
- Authorized (role-specific): everything under `/manage/*` and `/admin/*` routes above

The JWT is stored in `localStorage` (`rm_token`) and attached to every request by the axios interceptor in
`src/api/client.js`; a 401 response clears the session and redirects to `/login`.
