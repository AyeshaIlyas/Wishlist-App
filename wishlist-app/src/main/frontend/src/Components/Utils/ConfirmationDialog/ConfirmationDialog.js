import "./ConfirmationDialog.css";

export default function ConfirmationDialog({title, details, actionLabel, action, actionArguments={}, cancel}) {

    const handleSubmit = e => {
        e.preventDefault();
        action(actionArguments);
    }

    return (
        <form className="ConfirmationDialog" onSubmit={handleSubmit}>
            <div className="ConfirmationDialog-container">
                 <h3>{title}</h3>
                <p>{details}</p>
                <div className="ConfirmationDialog-controls">
                    <button>{actionLabel}</button>
                    <button onClick={cancel} type="button">Cancel</button>
                </div>
            </div>
           
        </form>
    )
}