import {Route, Routes} from 'react-router-dom';
import Navbar from './components/Navbar';
import {RequireAuth, RequireRole} from './components/RouteGuards';

import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import HomePage from './pages/HomePage';
import AboutPage from './pages/AboutPage';
import CatalogPage from './pages/CatalogPage';
import ManuscriptDetailPage from './pages/ManuscriptDetailPage';
import MyRequestsPage from './pages/MyRequestsPage';
import MyReservationsPage from './pages/MyReservationsPage';
import ProfilePage from './pages/ProfilePage';
import RequestManagementPage from './pages/RequestManagementPage';
import ManuscriptManagementPage from './pages/ManuscriptManagementPage';
import ReservationsOverviewPage from './pages/ReservationsOverviewPage';
import DigitizationDashboardPage from './pages/DigitizationDashboardPage';
import AdminUsersPage from './pages/AdminUsersPage';
import OAuthCallbackPage from './pages/OAuthCallbackPage';
import ResetPasswordPage from "./pages/ResetPasswordPage.jsx";
import ForgotPasswordPage from "./pages/ForgotPasswordPage.jsx";

function Layout({children}) {
    return (
        <div className="app-shell">
            <Navbar/>
            {children}
        </div>
    );
}

export default function App() {
    return (
        <Routes>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/register" element={<RegisterPage/>}/>
            <Route path="/oauth-callback" element={<OAuthCallbackPage/>}/>

            <Route path="/" element={<Layout><HomePage/></Layout>}/>
            <Route path="/about" element={<Layout><AboutPage/></Layout>}/>
            <Route path="/catalog" element={<Layout><CatalogPage/></Layout>}/>
            <Route path="/catalog/:id" element={<Layout><ManuscriptDetailPage/></Layout>}/>

            <Route path="/my-requests" element={<Layout><RequireAuth><MyRequestsPage/></RequireAuth></Layout>}/>
            <Route path="/my-reservations" element={<Layout><RequireAuth><MyReservationsPage/></RequireAuth></Layout>}/>
            <Route path="/profile" element={<Layout><RequireAuth><ProfilePage/></RequireAuth></Layout>}/>

            <Route
                path="/manage/requests"
                element={<Layout><RequireRole
                    roles={['CURATOR', 'ADMIN']}><RequestManagementPage/></RequireRole></Layout>}
            />
            <Route
                path="/manage/manuscripts"
                element={<Layout><RequireRole
                    roles={['CURATOR', 'ADMIN']}><ManuscriptManagementPage/></RequireRole></Layout>}
            />
            <Route
                path="/manage/reservations"
                element={<Layout><RequireRole
                    roles={['CURATOR', 'ADMIN']}><ReservationsOverviewPage/></RequireRole></Layout>}
            />
            <Route
                path="/manage/digitization"
                element={<Layout><RequireRole
                    roles={['CURATOR', 'ADMIN']}><DigitizationDashboardPage/></RequireRole></Layout>}
            />
            <Route
                path="/admin/users"
                element={<Layout><RequireRole roles={['ADMIN']}><AdminUsersPage/></RequireRole></Layout>}
            />

            <Route path="/forgot-password" element={<ForgotPasswordPage/>}/>

            <Route path="/reset-password" element={<ResetPasswordPage/>}/>
        </Routes>
    );
}
