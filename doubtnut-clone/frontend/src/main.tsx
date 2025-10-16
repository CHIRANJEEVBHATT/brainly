import React from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom'
import { GoogleOAuthProvider } from '@react-oauth/google'
import './index.css'

function getUser() {
	const raw = localStorage.getItem('user')
	return raw ? JSON.parse(raw) : null
}

function Layout({ children }: { children: React.ReactNode }) {
	const user = getUser()
	return (
		<div className="min-h-screen bg-gray-50">
			<nav className="bg-white border-b">
				<div className="max-w-3xl mx-auto px-4 py-3 flex items-center justify-between">
					<div className="flex gap-4">
						<Link to="/" className="font-semibold">Home</Link>
						<Link to="/ask" className="text-blue-600">Ask Question</Link>
					</div>
					<div>
						{user ? (
							<div className="flex items-center gap-2">
								{user.pictureUrl ? <img src={user.pictureUrl} className="w-8 h-8 rounded-full"/> : null}
								<span className="text-sm text-gray-700">{user.name}</span>
							</div>
						) : (
							<Link to="/login" className="text-blue-600">Login</Link>
						)}
					</div>
				</div>
			</nav>
			<main className="max-w-3xl mx-auto p-4">
				{children}
			</main>
		</div>
	)
}

function LoginPage() {
	return (
		<div className="flex flex-col items-center justify-center py-16">
			<h1 className="text-2xl font-semibold mb-6">Login</h1>
			<LoginButton/>
		</div>
	)
}

function LoginButton() {
	const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID as string | undefined
	if (!clientId) {
		return <div className="text-sm text-red-600">Set VITE_GOOGLE_CLIENT_ID in .env</div>
	}
	return (
		<GoogleOAuthProvider clientId={clientId}>
			<GoogleLoginInner/>
		</GoogleOAuthProvider>
	)
}

function GoogleLoginInner() {
	const { GoogleLogin } = require('@react-oauth/google') as typeof import('@react-oauth/google')
	async function onSuccess(cred: any) {
		try {
			const jwt_decode = (await import('jwt-decode')).default as any
			const payload = jwt_decode(cred.credential)
			const user = {
				name: payload.name,
				email: payload.email,
				pictureUrl: payload.picture
			}
			localStorage.setItem('user', JSON.stringify(user))
			await apiPost('/api/auth/google', user)
			window.location.href = '/'
		} catch (e) {
			console.error(e)
			alert('Login failed')
		}
	}
	return <GoogleLogin onSuccess={onSuccess} onError={() => alert('Login error')}/>
}

async function apiGet(path: string) {
	const base = 'http://localhost:8080'
	const res = await fetch(base + path, { credentials: 'include' })
	if (!res.ok) throw new Error('Request failed')
	return res.json()
}

async function apiPost(path: string, body: any) {
	const base = 'http://localhost:8080'
	const res = await fetch(base + path, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		credentials: 'include',
		body: JSON.stringify(body)
	})
	if (!res.ok) throw new Error('Request failed')
	return res.json()
}

function HomePage() {
	const [loading, setLoading] = React.useState(true)
	const [items, setItems] = React.useState<any[]>([])
	const [answerText, setAnswerText] = React.useState<{[k:number]:string}>({})
	const user = getUser()
	React.useEffect(() => {
		apiGet('/api/questions').then(setItems).finally(() => setLoading(false))
	}, [])
	async function addAnswer(q: any) {
		if (!user) return window.location.href = '/login'
		const content = answerText[q.id] || ''
		if (!content.trim()) return
		await apiPost('/api/answers', { questionId: q.id, content, userId: user.id || 0 })
		const fresh = await apiGet('/api/questions')
		setItems(fresh)
		setAnswerText(prev => ({...prev, [q.id]: ''}))
	}
	if (loading) return <div>Loading...</div>
	return (
		<div className="space-y-4">
			{items.map(q => (
				<div key={q.id} className="bg-white border rounded p-4">
					<h3 className="font-semibold text-lg">{q.title}</h3>
					<p className="text-gray-700 whitespace-pre-wrap">{q.description}</p>
					<p className="text-xs text-gray-500 mt-1">Asked by {q.userName || 'Unknown'}</p>
					<div className="mt-3 space-y-2">
						<p className="font-medium">Answers</p>
						{q.answers?.length ? q.answers.map((a:any) => (
							<div key={a.id} className="text-sm text-gray-800">
								{a.content}
								<span className="text-xs text-gray-500"> — {a.userName || 'Anon'}</span>
							</div>
						)) : <div className="text-sm text-gray-500">No answers yet.</div>}
					</div>
					<div className="mt-3">
						<textarea className="w-full border rounded p-2 text-sm" rows={2} placeholder="Add an answer"
							value={answerText[q.id] || ''}
							onChange={e => setAnswerText(prev => ({...prev, [q.id]: e.target.value}))}
						/>
						<button className="mt-2 px-3 py-1 bg-blue-600 text-white rounded text-sm" onClick={() => addAnswer(q)}>
							Add Answer
						</button>
					</div>
				</div>
			))}
		</div>
	)
}

function AskPage() {
	const [title, setTitle] = React.useState('')
	const [description, setDescription] = React.useState('')
	const user = getUser()
	async function submit() {
		if (!user) return window.location.href = '/login'
		if (!title.trim() || !description.trim()) return
		await apiPost('/api/questions', { title, description, userId: user.id || 0 })
		window.location.href = '/'
	}
	return (
		<div className="max-w-2xl">
			<h1 className="text-xl font-semibold mb-4">Ask a Question</h1>
			<input className="w-full border rounded p-2 mb-3" placeholder="Title" value={title} onChange={e=>setTitle(e.target.value)} />
			<textarea className="w-full border rounded p-2 mb-3" rows={6} placeholder="Description" value={description} onChange={e=>setDescription(e.target.value)} />
			<button className="px-4 py-2 bg-blue-600 text-white rounded" onClick={submit}>Submit</button>
		</div>
	)
}

function AppRoutes() {
	return (
		<BrowserRouter>
			<Routes>
				<Route path="/" element={<Layout><HomePage/></Layout>} />
				<Route path="/ask" element={<Layout><AskPage/></Layout>} />
				<Route path="/login" element={<LoginPage/>} />
				<Route path="*" element={<Navigate to="/"/>} />
			</Routes>
		</BrowserRouter>
	)
}

const container = document.getElementById('app')!
createRoot(container).render(<AppRoutes />)


