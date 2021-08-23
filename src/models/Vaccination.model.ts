import Patient from "./Patient.model";

interface Vaccination {
  id: string;
  date: string;
  time: string;
  address: {
    institution_name: string;
    street: string;
    city: string;
    state: string;
    zip: string;
  };
  patients: Patient[];
}

export default Vaccination;
