import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';

export default function Spinner({size="3x", color="#395676"}) {
    return <FontAwesomeIcon className="Spinner" icon={faSpinner} pulse size={size} color={color}/>
}



