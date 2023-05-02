import "./Announcement.css";

export default function Announcement({message, type="info", cancel, vPos}) {
    
    const colorClass = (type === "error") ? "Announcement-error" : "Announcement-info";
    return (
        <div className={`Announcement ${colorClass}`} style={{top: `${5 + 3 * vPos}em`}}>
            <div className="Announcement-container">
                <span className="Announcement-msg">{message}</span>
                <span className="Announcement-x"onClick={cancel}>x</span>
            </div>
        </div>
    )
}